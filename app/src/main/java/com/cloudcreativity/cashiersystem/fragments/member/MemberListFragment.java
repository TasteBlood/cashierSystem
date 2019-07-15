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
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberListBinding;
import com.cloudcreativity.cashiersystem.model.MemberListModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 会员列表页
 * */
public class MemberListFragment extends LazyFragment {

    private FragmentMemberListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list,container,false);
        binding.setModel(new MemberListModel(context, binding,this));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

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

    @Subscribe
    public void onEvent(String msg){
        if("refresh_member_list".equals(msg)){
            binding.refreshMember.startRefresh();
        }
    }
}
