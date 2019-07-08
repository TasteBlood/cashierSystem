package com.cloudcreativity.cashiersystem.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberBinding;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MemberFragment extends LazyFragment {

    private MemberModel memberModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMemberBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member,container,false);
        binding.setModel(memberModel = new MemberModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String name){
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_DETAIL_INDEX.equals(name)){
            //切换到详情
            memberModel.onDetail();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_LIST.equals(name)){
            memberModel.onList();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_EDIT.equals(name)){
            memberModel.onEdit();
        }
    }
}
