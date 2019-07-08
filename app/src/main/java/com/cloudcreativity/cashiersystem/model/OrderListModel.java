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
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOrderBinding;
import com.cloudcreativity.cashiersystem.entity.OrderEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderListModel extends BaseModel<FragmentActivity, FragmentOrderListBinding> {

    private BaseDialogImpl baseDialog;

    public BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding> adapter;

    public ObservableField<String> selectDate = new ObservableField<>();

    private RadioButton lastCheck1;
    private RadioButton lastCheck2;
    private RadioButton lastCheck3;
    private RadioButton lastCheck4;
    private RadioButton lastCheck5;

    public OrderListModel(FragmentActivity context, FragmentOrderListBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        binding.rcvOrderList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        loadData();
    }

    private void loadData(){
        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_order;
            }

            @Override
            protected void onBindItem(ItemLayoutOrderBinding binding, OrderEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_DETAIL);
                    }
                });
            }
        };

        List<OrderEntity> entities = new ArrayList<>();
        for(int i=0;i<=20;i++){
            OrderEntity entity = new OrderEntity();
            entity.setId("620101100119061110001");
            entity.setMoney(20000.0);
            entity.setCreateTime("2019-06-11 12:30:00");
            entity.setPayway("1");
            entity.setState(0);
            entities.add(entity);
        }

        adapter.getItems().addAll(entities);
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
            return;
        }
        binding.rgPayWay.check(rb.getId());
        lastCheck1 = rb;
    }
    public void onOptionClick2(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck2!=null&&lastCheck2.getId()==rb.getId()){
            binding.rgState.clearCheck();
            lastCheck2 = null;
            return;
        }
        binding.rgState.check(rb.getId());
        lastCheck2 = rb;
    }
    public void onOptionClick3(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck3!=null&&lastCheck3.getId()==rb.getId()){
            binding.rgIdentity.clearCheck();
            lastCheck1 = null;
            return;
        }
        binding.rgIdentity.check(rb.getId());
        lastCheck3 = rb;
    }
    public void onOptionClick4(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck4!=null&&lastCheck4.getId()==rb.getId()){
            binding.rgBack.clearCheck();
            lastCheck4 = null;
            return;
        }
        binding.rgBack.check(rb.getId());
        lastCheck4 = rb;
    }
    public void onOptionClick5(View view){
        RadioButton rb = (RadioButton) view;
        //binding.rgIdentity.clearCheck();
        if(lastCheck5!=null&&lastCheck5.getId()==rb.getId()){
            binding.rgTime.clearCheck();
            lastCheck5 = null;
            return;
        }
        binding.rgTime.check(rb.getId());
        lastCheck5 = rb;
    }

    public void onTimeClick(){
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectDate.set(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void onSearchClick(){
        ToastUtils.showShortToast(context,"搜索了");
    }
}
