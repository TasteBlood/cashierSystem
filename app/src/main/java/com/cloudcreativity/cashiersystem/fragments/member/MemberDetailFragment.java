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
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberDetailBinding;
import com.cloudcreativity.cashiersystem.model.MemberDetailModel;

/**
 * 会员详情页
 */
public class MemberDetailFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMemberDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_detail,container,false);
        binding.setModel(new MemberDetailModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
