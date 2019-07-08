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
import com.cloudcreativity.cashiersystem.databinding.FragmentMessageBinding;
import com.cloudcreativity.cashiersystem.entity.MessageEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MessageFragment extends LazyFragment {

    private MessageModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMessageBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message,container,false);
        binding.getRoot().setClickable(true);
        model = new MessageModel(context, binding);
        binding.setModel(model);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

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

    @Subscribe
    public void onEvent(String name){
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_MESSAGE_LIST.equals(name)){
            model.onDefault();
        }
    }

    @Subscribe
    public void onEvent(MessageEntity entity){
        model.onDetail(entity);
    }
}
