package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentLogBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutCashierLogBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutCreateLogBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutEditLogBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutRechargeLogBinding;
import com.cloudcreativity.cashiersystem.entity.CashLogEntity;
import com.cloudcreativity.cashiersystem.entity.CreateLogEntity;
import com.cloudcreativity.cashiersystem.entity.EditLogEntity;
import com.cloudcreativity.cashiersystem.entity.RechargeLogEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.MemberRechargeLogUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogModel extends BaseModel<FragmentActivity, FragmentLogBinding> {
    private BaseDialogImpl baseDialog;

    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> exitTime = new ObservableField<>();

    private int cash_page = 1;
    private int create_page = 1;
    private int edit_page  =1;
    private int recharge_page = 2;

    private int size = 20;

    private long adminId = Long.parseLong(SPUtils.get().getUid());

    public BaseBindingRecyclerViewAdapter<CashLogEntity, ItemLayoutCashierLogBinding> cashAdapter;
    public BaseBindingRecyclerViewAdapter<CreateLogEntity, ItemLayoutCreateLogBinding> createAdapter;
    public BaseBindingRecyclerViewAdapter<EditLogEntity, ItemLayoutEditLogBinding> editAdapter;
    public BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutRechargeLogBinding> rechargeAdapter;
    public LogModel(FragmentActivity context, final FragmentLogBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        initView();
        initData();
        startTime.set(BaseApp.LOGIN_TIME);
        exitTime.set(BaseApp.LOGOUT_TIME);
        binding.rgType.check(R.id.rb_log_cashier);
        binding.layoutCash.setVisibility(View.VISIBLE);
        binding.refreshCashier.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.refreshCashier.startRefresh();
            }
        },500);
    }

    private void initView(){
        binding.rcvCashier.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvCreate.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvEdit.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        binding.rcvRecharge.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        binding.refreshCashier.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                cash_page  =1;
                loadCashLog();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadCashLog();
            }
        });
        binding.refreshCreate.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                create_page  =1;
                loadCreateLog();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadCreateLog();
            }
        });
        binding.refreshEdit.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                edit_page  =1;
                loadEditLog();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadEditLog();
            }
        });
        binding.refreshRecharge.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                recharge_page  =1;
                loadRechargeLog();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadRechargeLog();
            }
        });

    }

    private void initData(){
        cashAdapter = new BaseBindingRecyclerViewAdapter<CashLogEntity, ItemLayoutCashierLogBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_cashier_log;
            }

            @Override
            protected void onBindItem(ItemLayoutCashierLogBinding binding, CashLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        createAdapter = new BaseBindingRecyclerViewAdapter<CreateLogEntity, ItemLayoutCreateLogBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_create_log;
            }

            @Override
            protected void onBindItem(ItemLayoutCreateLogBinding binding, CreateLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        editAdapter = new BaseBindingRecyclerViewAdapter<EditLogEntity, ItemLayoutEditLogBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_edit_log;
            }

            @Override
            protected void onBindItem(ItemLayoutEditLogBinding binding, EditLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        rechargeAdapter = new BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutRechargeLogBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_recharge_log;
            }

            @Override
            protected void onBindItem(ItemLayoutRechargeLogBinding binding, final RechargeLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MemberRechargeLogUtils(baseDialog,context)
                                .show(item.getMemberId(),item.getMobile());
                    }
                });
            }
        };
    }

    public void onBack() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
        BaseApp.LOGOUT_TIME = "";
        BaseApp.LOGIN_TIME = "";
    }

    private void loadCashLog(){
        HttpUtils.getInstance().getCashLog(adminId,startTime.get(),exitTime.get(),cash_page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<CashLogEntity>>() {
                        }.getType();
                        BaseResult<CashLogEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(cash_page==1){
                                binding.refreshCashier.finishRefreshing();
                                cashAdapter.getItems().clear();
                            }else{
                                binding.refreshCashier.finishLoadmore();
                            }
                            cashAdapter.getItems().addAll(result.getRecords());
                            cash_page ++;
                        }else{
                            if(cash_page==1){
                                binding.refreshCashier.finishRefreshing();
                                cashAdapter.getItems().clear();
                            }else{
                                binding.refreshCashier.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(cash_page==1){
                            binding.refreshCashier.finishRefreshing();
                        }else{
                            binding.refreshCashier.finishLoadmore();
                        }
                    }
                });
    }

    private void loadCreateLog(){
        HttpUtils.getInstance().getCreateLog(adminId,startTime.get(),exitTime.get(),create_page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<CreateLogEntity>>() {
                        }.getType();
                        BaseResult<CreateLogEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(create_page==1){
                                binding.refreshCreate.finishRefreshing();
                                createAdapter.getItems().clear();
                            }else{
                                binding.refreshCreate.finishLoadmore();
                            }
                            createAdapter.getItems().addAll(result.getRecords());
                            create_page ++;
                        }else{
                            if(create_page==1){
                                binding.refreshCreate.finishRefreshing();
                                createAdapter.getItems().clear();
                            }else{
                                binding.refreshCreate.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(create_page==1){
                            binding.refreshCreate.finishRefreshing();
                        }else{
                            binding.refreshCreate.finishLoadmore();
                        }
                    }
                });
    }

    private void loadEditLog(){
        HttpUtils.getInstance().getEditLog(adminId,startTime.get(),exitTime.get(),edit_page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<EditLogEntity>>() {
                        }.getType();
                        BaseResult<EditLogEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(edit_page==1){
                                binding.refreshEdit.finishRefreshing();
                                editAdapter.getItems().clear();
                            }else{
                                binding.refreshEdit.finishLoadmore();
                            }
                            editAdapter.getItems().addAll(result.getRecords());
                            edit_page ++;
                        }else{
                            if(create_page==1){
                                binding.refreshEdit.finishRefreshing();
                                editAdapter.getItems().clear();
                            }else{
                                binding.refreshEdit.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(edit_page==1){
                            binding.refreshEdit.finishRefreshing();
                        }else{
                            binding.refreshEdit.finishLoadmore();
                        }
                    }
                });
    }

    private void loadRechargeLog(){
        HttpUtils.getInstance().getRechargeLog(adminId,startTime.get(),exitTime.get(),recharge_page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<RechargeLogEntity>>() {
                        }.getType();
                        BaseResult<RechargeLogEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(recharge_page==1){
                                binding.refreshRecharge.finishRefreshing();
                                rechargeAdapter.getItems().clear();
                            }else{
                                binding.refreshRecharge.finishLoadmore();
                            }
                            rechargeAdapter.getItems().addAll(result.getRecords());
                            recharge_page ++;
                        }else{
                            if(recharge_page==1){
                                binding.refreshRecharge.finishRefreshing();
                                rechargeAdapter.getItems().clear();
                            }else{
                                binding.refreshRecharge.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(recharge_page==1){
                            binding.refreshRecharge.finishRefreshing();
                        }else{
                            binding.refreshRecharge.finishLoadmore();
                        }
                    }
                });
    }

    public void onCheckChange(RadioGroup rg, int id) {
        switch (id) {
            case R.id.rb_log_cashier:
                binding.layoutCash.setVisibility(View.VISIBLE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.GONE);
                if(cashAdapter.getItems().size()<=0)
                    binding.refreshCashier.startRefresh();
                break;
            case R.id.rb_log_create:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.VISIBLE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.GONE);
                if(createAdapter.getItems().size()<=0)
                    binding.refreshCreate.startRefresh();
                break;
            case R.id.rb_log_edit:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.VISIBLE);
                binding.layoutRecharge.setVisibility(View.GONE);
                if(editAdapter.getItems().size()<=0)
                    binding.refreshEdit.startRefresh();
                break;
            case R.id.rb_log_recharge:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.VISIBLE);
                if(rechargeAdapter.getItems().size()<=0)
                    binding.refreshRecharge.startRefresh();
                break;
        }
    }
}
