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
import com.cloudcreativity.cashiersystem.databinding.FragmentBalanceBinding;
import com.cloudcreativity.cashiersystem.model.MemberBalanceModel;

/**
 * 充值记录详情页
 */
public class BalanceFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBalanceBinding balanceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance,container,false);
        balanceBinding.setModel(new MemberBalanceModel(context,balanceBinding,this));
        balanceBinding.getRoot().setClickable(true);
        return balanceBinding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
