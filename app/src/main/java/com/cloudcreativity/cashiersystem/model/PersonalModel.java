package com.cloudcreativity.cashiersystem.model;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentPersonalBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutPersonalLogBinding;
import com.cloudcreativity.cashiersystem.entity.LogEntity;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.cloudcreativity.cashiersystem.receiver.MyBusinessReceiver;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PersonalModel extends BaseModel<FragmentActivity, FragmentPersonalBinding> {

    public BaseBindingRecyclerViewAdapter<LogEntity, ItemLayoutPersonalLogBinding> adapter;

    public ObservableField<String> selectDate = new ObservableField<>();

    public ObservableField<UserEntity> user = new ObservableField<>();

    public ObservableField<String> lastLogin = new ObservableField<>();

    private int pageNum = 1;
    private int pageSize = 20;
    private BaseDialogImpl baseDialog;
    public PersonalModel(FragmentActivity context, final FragmentPersonalBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        initData();
        user.set(SPUtils.get().getUser());
        binding.refreshLogs.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.refreshLogs.startRefresh();
            }
        },500);
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        selectDate.set(instance.get(Calendar.YEAR)+"-"+formatZero((instance.get(Calendar.MONTH)+1))+"-"+formatZero(instance.get(Calendar.DAY_OF_MONTH)));
    }

    public void onLogout() {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("确定退出登录吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assert user.get()!=null;
                        HttpUtils.getInstance().logout(user.get().getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        ToastUtils.showShortToast(context, "退出登录");
                                        Intent intent = new Intent();
                                        intent.setAction(MyBusinessReceiver.ACTION_LOGOUT);
                                        context.sendBroadcast(intent);
                                    }

                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void onChangePwd() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_CHANGE_PWD);
    }

    public void onChangeMobile(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_CHANGE_MOBILE);
    }

    private void initData() {
        binding.rcvPersonalLog.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new BaseBindingRecyclerViewAdapter<LogEntity, ItemLayoutPersonalLogBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_personal_log;
            }

            @Override
            protected void onBindItem(ItemLayoutPersonalLogBinding binding, LogEntity item, int position) {
                binding.setItem(item);
                if (position % 2 == 1) {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                } else {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };
        binding.refreshLogs.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(pageNum,pageSize,selectDate.get());
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(pageNum,pageSize,selectDate.get());
            }
        });
    }

    private void loadData(final int page, int size, String time){
        HttpUtils.getInstance().getLoginLog(page,size,time,user.get().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<LogEntity>>() {
                        }.getType();
                        BaseResult<LogEntity> result = new Gson().fromJson(t, type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(page==1){
                                lastLogin.set(result.getRecords().get(0).getLoginTime());
                                adapter.getItems().clear();
                                binding.refreshLogs.finishRefreshing();
                            }else{
                                binding.refreshLogs.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum ++;
                        }else{
                            if(page==1){
                                adapter.getItems().clear();
                                binding.refreshLogs.finishRefreshing();
                            }else{
                                binding.refreshLogs.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshLogs.finishRefreshing();
                        }else{
                            binding.refreshLogs.finishLoadmore();
                        }
                    }
                });
    }

    public void onDateClick(){
        showDatePicker();
    }

    //搜索
    public void onSearchClick(){
        binding.refreshLogs.startRefresh();
    }

    private void showDatePicker() {
        Calendar instance = Calendar.getInstance(Locale.CHINA);
        DatePickerDialog dialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                   selectDate.set(formatZero(year)+"-"+formatZero((month+1))+"-"+formatZero(dayOfMonth));
            }
        }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));

        instance.add(Calendar.DAY_OF_MONTH,1);
        dialog.getDatePicker().setMaxDate(instance.getTimeInMillis());
        Window window = dialog.getWindow();
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        assert window != null;
        window.getAttributes().width = widthPixels;
        window.getAttributes().height = widthPixels/2;

        dialog.show();
    }

    private String formatZero(int a){
        return a<10?"0"+a:""+a;
    }
}
