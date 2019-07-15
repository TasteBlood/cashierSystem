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
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class OrderIndexFragment extends LazyFragment {

    private OrderIndexModel indexModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        LogUtils.e("xuxiwu","order index created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtils.e("xuxiwu","order index destroy");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOrderIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_index,container,false);
        binding.setModel(indexModel = new OrderIndexModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }

    @Subscribe
    public void onEvent(String name){
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_LIST.equals(name)){
            //展示列表
            if(indexModel!=null){
                indexModel.onDefault();
            }
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_DETAIL.equals(name)){
            //展示详情
            if(indexModel!=null){
                indexModel.onDetail();
            }

        }
    }
}
