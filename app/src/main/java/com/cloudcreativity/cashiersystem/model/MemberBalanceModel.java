package com.cloudcreativity.cashiersystem.model;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentBalanceBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberRechargeBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutMemberRechargeUtilsBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.RechargeLogEntity;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberBalanceModel extends BaseModel<FragmentActivity, FragmentBalanceBinding>{

    private BaseDialogImpl baseDialog;

    public MemberEntity memberEntity;

    public BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutMemberRechargeBinding> adapter;

    private int pageNum = 1;
    private int pageSize = 20;

    public MemberBalanceModel(FragmentActivity context, FragmentBalanceBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        this.memberEntity = BaseApp.ENTITY;
        adapter = new BaseBindingRecyclerViewAdapter<RechargeLogEntity, ItemLayoutMemberRechargeBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_member_recharge;
            }

            @Override
            protected void onBindItem(ItemLayoutMemberRechargeBinding binding, RechargeLogEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        binding.refreshRecharge.setOnRefreshListener(new RefreshListenerAdapter() {
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
        binding.rcvRecharge.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        binding.refreshRecharge.startRefresh();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL);
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
}
