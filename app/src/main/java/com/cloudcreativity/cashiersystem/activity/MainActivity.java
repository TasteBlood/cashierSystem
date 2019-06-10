package com.cloudcreativity.cashiersystem.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.databinding.ActivityMainBinding;
import com.cloudcreativity.cashiersystem.model.MainActivityModel;

public class MainActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setModel(new MainActivityModel(this,binding));
    }
}
