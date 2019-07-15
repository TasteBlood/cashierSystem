package com.cloudcreativity.cashiersystem.model;

import android.app.DatePickerDialog;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOrderBinding;
import com.cloudcreativity.cashiersystem.entity.OrderEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderListModel extends BaseModel<FragmentActivity, FragmentOrderListBinding> {

    private BaseDialogImpl baseDialog;

    public BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding> adapter;

    public ObservableField<String> selectDate = new ObservableField<>();
    public ObservableField<String> key = new ObservableField<>();

    private RadioButton lastCheck1;
    private RadioButton lastCheck2;
    private RadioButton lastCheck3;
    private RadioButton lastCheck4;
    private RadioButton lastCheck5;

    private Integer payWay;
    private Integer payState;
    private Integer identityType;
    private Integer backState;
    private Integer timeType;

    private int pageNum = 1;
    private int pageSize = 20;

    public OrderListModel(FragmentActivity context, final FragmentOrderListBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        binding.rcvOrderList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        initData();
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH)+1;
        int day = instance.get(Calendar.DAY_OF_MONTH);

        selectDate.set(year+"-"+formatZero(month)+"-"+formatZero(day));

        binding.refreshOrder.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.refreshOrder.startRefresh();
            }
        },500);
    }

    private void initData(){
        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_order;
            }

            @Override
            protected void onBindItem(ItemLayoutOrderBinding binding, final OrderEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseApp.CURRENT_OID = item.getId();
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_DETAIL);
                    }
                });
            }
        };
        binding.refreshOrder.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(selectDate.get(),payWay,identityType,timeType,key.get(),pageNum,pageSize);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(selectDate.get(),payWay,identityType,timeType,key.get(),pageNum,pageSize);
            }
        });
    }

    private void loadData(String time, Integer payWay,Integer identityType,
                          Integer timeType, String key, final int page, int size){
        HttpUtils.getInstance().getOrders(time,identityType,key,payWay,timeType,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<OrderEntity>>() {
                        }.getType();
                        BaseResult<OrderEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()==null||result.getRecords().size()==0){
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshOrder.finishRefreshing();
                            }else{
                                binding.refreshOrder.finishLoadmore();
                            }
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshOrder.finishRefreshing();
                            }else{
                                binding.refreshOrder.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum ++;
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshOrder.finishRefreshing();
                        }else{
                            binding.refreshOrder.finishLoadmore();
                        }
                    }
                });

    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER);
    }

    public void onOptionClick1(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck1!=null&&lastCheck1.getId()==rb.getId()){
            binding.rgPayWay.clearCheck();
            lastCheck1 = null;
            payWay = null;
            binding.refreshOrder.startRefresh();
            return;
        }
        binding.rgPayWay.check(rb.getId());
        lastCheck1 = rb;
        if(lastCheck1.getId()==R.id.rb_cash){
            payWay = 2;
        }else if(lastCheck1.getId()==R.id.rb_phone){
            payWay = 3;
        }
        binding.refreshOrder.startRefresh();
    }
    public void onOptionClick2(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck2!=null&&lastCheck2.getId()==rb.getId()){
            binding.rgState.clearCheck();
            lastCheck2 = null;
            payState = null;
            binding.refreshOrder.startRefresh();
            return;
        }
        binding.rgState.check(rb.getId());
        lastCheck2 = rb;
        if(lastCheck2.getId()==R.id.rb_payed){
            payState = 1;
        }else if(lastCheck2.getId()==R.id.rb_unpay){
            payState = 0;
        }
        binding.refreshOrder.startRefresh();
    }
    public void onOptionClick3(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck3!=null&&lastCheck3.getId()==rb.getId()){
            binding.rgIdentity.clearCheck();
            lastCheck3 = null;
            identityType = null;
            binding.refreshOrder.startRefresh();
            return;
        }
        binding.rgIdentity.check(rb.getId());
        lastCheck3 = rb;
        if(lastCheck3.getId()==R.id.rb_family){
            identityType = 1;
        }else if(lastCheck3.getId()==R.id.rb_vip){
            identityType = 2;
        }
        binding.refreshOrder.startRefresh();
    }
    public void onOptionClick4(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck4!=null&&lastCheck4.getId()==rb.getId()){
            binding.rgBack.clearCheck();
            lastCheck4 = null;
            backState = null;
            //binding.refreshOrder.startRefresh();
            return;
        }
        binding.rgBack.check(rb.getId());
        lastCheck4 = rb;
        if(lastCheck4.getId()==R.id.rb_yes){
            backState = 1;
        }else if(lastCheck4.getId()==R.id.rb_no){
            backState = 0;
        }
    }
    public void onOptionClick5(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck5!=null&&lastCheck5.getId()==rb.getId()){
            binding.rgTime.clearCheck();
            lastCheck5 = null;
            timeType = null;
            binding.refreshOrder.startRefresh();
            return;
        }
        binding.rgTime.check(rb.getId());
        lastCheck5 = rb;
        if(lastCheck5.getId()==R.id.rb_am){
            timeType = 1;
        }else if(lastCheck5.getId()==R.id.rb_pm){
            timeType = 2;
        }
        binding.refreshOrder.startRefresh();
    }

    public void onTimeClick(){
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectDate.set(year+"-"+formatZero(month+1)+"-"+formatZero(dayOfMonth));
                binding.refreshOrder.startRefresh();
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    public void onSearchClick(){
        pageNum = 1;
        binding.refreshOrder.startRefresh();
    }

    private String formatZero(int num){
        return num<10?"0"+num:""+num;
    }
}
