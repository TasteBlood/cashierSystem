package com.cloudcreativity.cashiersystem.utils;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberRechargeUtilsBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOpenOrderBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutPayDetailBinding;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogMemberPayDetailBinding;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogMemberRechargeLogBinding;
import com.cloudcreativity.cashiersystem.entity.MemberPayEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.entity.PayDetailEntity;
import com.cloudcreativity.cashiersystem.entity.RechargeLogEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberPayDetailDialogUtils {
    private Dialog dialog;
    private BaseDialogImpl baseDialog;
    public ObservableField<Long> mid = new ObservableField<>();
    public ObservableField<String> mobile = new ObservableField<>();
    private LayoutDialogMemberPayDetailBinding binding;
    public ObservableField<PayDetailEntity> payEntity = new ObservableField<>();

    public BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutPayDetailBinding> adapter;
    private Context context;

    public MemberPayDetailDialogUtils(BaseDialogImpl baseDialog, Context context) {
        this.baseDialog = baseDialog;
        this.context = context;
        adapter = new BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutPayDetailBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_pay_detail;
            }

            @Override
            protected void onBindItem(ItemLayoutPayDetailBinding binding, OpenOrderGoodsEntity item, int position) {
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

    private void loadData(int cid){
        HttpUtils.getInstance().queryMemberPayDetail(cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        PayDetailEntity entity = new Gson().fromJson(t, PayDetailEntity.class);
                        if(entity!=null&& !TextUtils.isEmpty(entity.getOrderId())){
                            payEntity.set(entity);
                            adapter.getItems().clear();
                            adapter.getItems().addAll(entity.getOrderDetails());
                            adapter.notifyDataSetChanged();
                        }
                        binding.refreshRecharge.finishRefreshing();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                            binding.refreshRecharge.finishRefreshing();
                    }
                });
    }

    public void show(final long mid,String mobile,final int cid){
        this.mid.set(mid);
        this.mobile.set(mobile);
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        binding =
                DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.layout_dialog_member_pay_detail,null,false);
        binding.setUtils(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(binding.getRoot());
        binding.refreshRecharge.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                loadData(cid);
            }
        });
        binding.rcvRecharge.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        Window window = dialog.getWindow();
        assert window != null;
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels/2;
        window.getAttributes().gravity = Gravity.CENTER;
        dialog.show();
        binding.refreshRecharge.startRefresh();
    }


}
