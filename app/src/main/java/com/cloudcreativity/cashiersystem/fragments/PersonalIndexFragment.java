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
import com.cloudcreativity.cashiersystem.databinding.FragmentPersonalIndexBinding;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PersonalIndexFragment extends LazyFragment {

    private PersonalIndexModel indexModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPersonalIndexBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_index,container,false);
        binding.setModel(indexModel = new PersonalIndexModel(context,binding));
        binding.getRoot().setClickable(true);
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
        if(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT.equals(name)){
            indexModel.onDefault();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_CHANGE_PWD.equals(name)){
            indexModel.onChangePwd();
        }else if(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_CHANGE_MOBILE.equals(name)){
            indexModel.onChangeMobile();
        }
    }
}
