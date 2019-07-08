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

/**
 * 会员列表页
 * */
public class MemberListFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMemberListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list,container,false);
        binding.setModel(new MemberListModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
