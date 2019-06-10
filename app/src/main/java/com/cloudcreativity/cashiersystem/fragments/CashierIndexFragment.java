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
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierIndexBinding;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CashierIndexFragment extends LazyFragment {

    private CashierIndexModel indexModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LogUtils.e("xuxiwu","CashierIndexFg create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtils.e("xuxiwu","CashierIndexFg destroy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCashierIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashier_index,container,false);
        binding.setModel(indexModel = new CashierIndexModel(context,binding));
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String fragmentName){
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_OPEN_ORDER.equals(fragmentName)){
            //切换到开单页面
            indexModel.onOpen();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER.equals(fragmentName)){
            //切换到订单页面
            indexModel.onQuery();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER.equals(fragmentName)){
            //切换到开单默认页面
            indexModel.onDefault();
        }
    }
}
