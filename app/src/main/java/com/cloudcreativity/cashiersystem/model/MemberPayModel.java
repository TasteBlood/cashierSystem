package com.cloudcreativity.cashiersystem.model;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentPayBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberPayBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberRechargeBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.MemberPayEntity;
import com.cloudcreativity.cashiersystem.entity.RechargeLogEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BaseResult;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.MemberPayDetailDialogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberPayModel extends BaseModel<FragmentActivity, FragmentPayBinding>{

    private BaseDialogImpl baseDialog;

    public MemberEntity memberEntity;

    public BaseBindingRecyclerViewAdapter<MemberPayEntity, ItemLayoutMemberPayBinding> adapter;

    private int pageNum = 1;
    private int pageSize = 20;

    public MemberPayModel(FragmentActivity context, FragmentPayBinding binding, final BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        this.memberEntity = BaseApp.ENTITY;
        adapter = new BaseBindingRecyclerViewAdapter<MemberPayEntity, ItemLayoutMemberPayBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_member_pay;
            }

            @Override
            protected void onBindItem(ItemLayoutMemberPayBinding binding, final MemberPayEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                binding.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MemberPayDetailDialogUtils(baseDialog,context).show(memberEntity.getId(),memberEntity.getMobile(),item.getId());
                    }
                });
            }
        };

        binding.refreshPay.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                loadData(memberEntity.getId(),pageNum,pageSize);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadData(memberEntity.getId(),pageNum,pageSize);
            }
        });
        binding.rcvPay.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        binding.refreshPay.startRefresh();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL);
    }

    private void loadData(long mid, final int page, int size){
        HttpUtils.getInstance().queryMemberPay(mid,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        Type type = new TypeToken<BaseResult<MemberPayEntity>>() {
                        }.getType();
                        BaseResult<MemberPayEntity> result = new Gson().fromJson(t,type);
                        if(result.getRecords()!=null&&result.getRecords().size()>0){
                            if(page==1){
                                binding.refreshPay.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshPay.finishLoadmore();
                            }
                            adapter.getItems().addAll(result.getRecords());
                            pageNum ++;
                        }else{
                            if(page==1){
                                binding.refreshPay.finishRefreshing();
                                adapter.getItems().clear();
                            }else{
                                binding.refreshPay.finishLoadmore();
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        if(page==1){
                            binding.refreshPay.finishRefreshing();
                        }else{
                            binding.refreshPay.finishLoadmore();
                        }
                    }
                });
    }
}
