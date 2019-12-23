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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CashierFragment extends LazyFragment {

    private CashierModel cashierModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // LogUtils.e("xuxiwu","cashierFg create");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // LogUtils.e("xuxiwu","cashierFg destroy");
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCashierBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashier,container,false);
        cashierModel = new CashierModel(context, binding);
        binding.setModel(cashierModel);
        cashierModel.initialize();
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.e("xuxiwu","cashier visible"+isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("xuxiwu","cashier resume");
    }

    @Subscribe
    public void onEvent(String msg){
        if("list_order_close".equals(msg)){
            if(getUserVisibleHint()){
                if(cashierModel!=null){
                    cashierModel.changeFragment("goodsInCashier");
                }
            }
        }
    }
}
