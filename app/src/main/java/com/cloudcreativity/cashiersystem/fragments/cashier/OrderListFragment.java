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
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderListBinding;
import com.cloudcreativity.cashiersystem.model.OrderListModel;

/**
 * 订单列表
 */
public class OrderListFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOrderListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_list,container,false);
        binding.setModel(new OrderListModel(context,binding,this));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
