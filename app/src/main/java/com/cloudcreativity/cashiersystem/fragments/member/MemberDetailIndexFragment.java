package com.cloudcreativity.cashiersystem.fragments.member;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberDetailIndexBinding;
import com.cloudcreativity.cashiersystem.model.MemberDetailIndexModel;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 会员详情索引页
 */
public class MemberDetailIndexFragment extends LazyFragment {

    private MemberDetailIndexModel indexModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LogUtils.e("xuxiwu","member detail index created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtils.e("xuxiwu","member detail index destroyed");
    }

    @Subscribe
    public void onEvent(String name){
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL.equals(name)){
            //展示详情
            indexModel.onDetail();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_BALANCE.equals(name)){
            //展示充值记录
            indexModel.onBalance();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_PAY.equals(name)){
            //消费记录
            indexModel.onPay();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_SCORE.equals(name)){
            //积分记录
            indexModel.onScore();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMemberDetailIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_detail_index, container, false);
        binding.setModel(indexModel = new MemberDetailIndexModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
