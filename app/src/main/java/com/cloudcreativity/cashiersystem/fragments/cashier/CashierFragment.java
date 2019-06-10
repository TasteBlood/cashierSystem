package com.cloudcreativity.cashiersystem.fragments.cashier;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierBinding;
import com.cloudcreativity.cashiersystem.model.CashierModel;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

public class CashierFragment extends LazyFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("xuxiwu","cashierFg create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("xuxiwu","cashierFg destroy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCashierBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashier,container,false);
        binding.setModel(new CashierModel());
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
