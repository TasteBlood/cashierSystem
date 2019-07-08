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
import com.cloudcreativity.cashiersystem.databinding.FragmentOpenOrderBinding;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.model.OpenOrderModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OpenOrderFragment extends LazyFragment {

    private OpenOrderModel model;


    public OpenOrderModel getModel() {
        return model;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOpenOrderBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_open_order, container, false);
        model = new OpenOrderModel(context, binding);
        binding.setModel(model);
        model.initialize();
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
    }

    @Subscribe
    public void onEvent(GoodsEntity goodsEntity){
        model.pushGoods(goodsEntity);
    }


}