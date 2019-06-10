package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.activity.MainActivity;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.ActivityMainBinding;
import com.cloudcreativity.cashiersystem.fragments.CashierIndexFragment;
import com.cloudcreativity.cashiersystem.fragments.MemberFragment;
import com.cloudcreativity.cashiersystem.fragments.MessageFragment;
import com.cloudcreativity.cashiersystem.fragments.PersonalFragment;
import com.cloudcreativity.cashiersystem.fragments.SettingFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class MainActivityModel extends BaseModel<MainActivity, ActivityMainBinding> {

    private FragmentManager manager;

    public MainActivityModel(MainActivity context, ActivityMainBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        RadioButton radioButton = (RadioButton) binding.rgMain.getChildAt(0);
        radioButton.setChecked(true);
        FGUtils.replace(manager,R.id.frameMain,new CashierIndexFragment());
    }

    public void onCheckChange(RadioGroup group,int checkId){
        //FragmentTransaction transaction = manager.beginTransaction();
        switch (checkId){
            case R.id.rb_cashier:
                FGUtils.replace(manager,R.id.frameMain,new CashierIndexFragment());
                //transaction.replace(R.id.frameMain,new CashierIndexFragment()).commit();
                break;
            case R.id.rb_news:
                FGUtils.replace(manager,R.id.frameMain,new MessageFragment());
                //transaction.replace(R.id.frameMain,new MessageFragment()).commit();
                break;
            case R.id.rb_member:
                FGUtils.replace(manager,R.id.frameMain,new MemberFragment());
                //transaction.replace(R.id.frameMain,new MemberFragment()).commit();
                break;
            case R.id.rb_setting:
                FGUtils.replace(manager,R.id.frameMain,new SettingFragment());
                //transaction.replace(R.id.frameMain,new SettingFragment()).commit();
                break;
            case R.id.rb_personal:
                FGUtils.replace(manager,R.id.frameMain,new PersonalFragment());
                //transaction.replace(R.id.frameMain,new PersonalFragment()).commit();
                break;
        }
    }
}
