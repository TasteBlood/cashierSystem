package com.cloudcreativity.cashiersystem.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentSettingBinding;
import com.cloudcreativity.cashiersystem.fragments.setting.SettingDefaultFragment;

public class SettingModel extends BaseModel<FragmentActivity, FragmentSettingBinding> {

    private FragmentManager manager;

    private SettingDefaultFragment defaultFragment;

    public SettingModel(FragmentActivity context, FragmentSettingBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        defaultFragment = new SettingDefaultFragment();
        onDefault();
    }

    void onDefault(){
        if(manager.findFragmentByTag("settingDefault")!=null){
            manager.beginTransaction()
                    .show(defaultFragment)
                    .commit();
        }else{
            manager.beginTransaction()
                    .add(R.id.frameSetting, defaultFragment,"settingDefault")
                    .show(defaultFragment)
                    .commit();
        }
    }


}
