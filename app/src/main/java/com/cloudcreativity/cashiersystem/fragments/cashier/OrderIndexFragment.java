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
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderIndexBinding;
import com.cloudcreativity.cashiersystem.model.OrderIndexModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OrderIndexFragment extends LazyFragment {
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
        FragmentOrderIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_index,container,false);
        binding.setModel(new OrderIndexModel(context,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String name){

    }
}
