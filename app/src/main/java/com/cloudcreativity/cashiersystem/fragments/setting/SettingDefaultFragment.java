package com.cloudcreativity.cashiersystem.fragments.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentSettingDefaultBinding;
import com.cloudcreativity.cashiersystem.model.SettingDefaultModel;

public class SettingDefaultFragment extends LazyFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSettingDefaultBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_default,container,false);
        binding.setModel(new SettingDefaultModel(context,binding));
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {

    }
}
