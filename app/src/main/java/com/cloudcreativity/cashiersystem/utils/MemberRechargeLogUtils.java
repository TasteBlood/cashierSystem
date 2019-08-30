package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberRechargeUtilsBinding;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogMemberRechargeLogBinding;
import com.cloudcreativity.cashiersystem.entity.RechargeLogEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberRechargeLogUtils {
    private Dialog dialog;
    private BaseDialogImpl baseDialog;
    public ObservableField<Long> mid = new ObservableField<>();
    public ObservableField<String> mobile = new ObservableField<>();
    private LayoutDialogMemberRechargeLogBinding binding;
    public BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutMemberRechargeUtilsBinding> adapter;
    private Context context;
    private int pageNum = 1;
    private int pageSize = 20;

    public MemberRechargeLogUtils(BaseDialogImpl baseDialog, Context context) {
        this.baseDialog = baseDialog;
        this.context = context;
        adapter = new BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutMemberRechargeUtilsBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_member_recharge_utils;
            }

            @Override
            protected void onBindItem(ItemLayoutMemberRechargeUtilsBinding binding, RechargeLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };
    }

    public void onClose(){
        if(this.dialog!=null)
            this.dialog.dismiss();
    }

    private void loadData(long mid, final int page, int size){
        HttpUtils.getInstance().queryMemberLog(mid,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<RechargeLogEntity>>() {
                        }.getType();
                        BaseResult<RechargeLogEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(page==1){
                                binding.refreshRecharge.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshRecharge.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum ++;
                        }else{
                            if(page==1){
                                binding.refreshRecharge.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshRecharge.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshRecharge.finishRefreshing();
                        }else{
                            binding.refreshRecharge.finishLoadmore();
                        }
                    }
                });
    }

    public void show(final long mid, String mobile){
        this.mid.set(mid);
        this.mobile.set(mobile);
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_member_recharge_log,null,false);
        binding.setUtils(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(binding.getRoot());
        binding.refreshRecharge.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(mid,pageNum,pageSize);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(mid,pageNum,pageSize);
            }
        });
        binding.rcvRecharge.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        Window window = dialog.getWindow();
        assert window != null;
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels*2/3;
        window.getAttributes().height = context.getResources().getDisplayMetrics().heightPixels*2/3;
        window.getAttributes().gravity = Gravity.CENTER;
        dialog.show();
        binding.refreshRecharge.startRefresh();
    }


}
