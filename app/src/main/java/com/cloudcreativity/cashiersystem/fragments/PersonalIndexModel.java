package com.cloudcreativity.cashiersystem.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentPersonalIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.personal.ChangeMobileFragment;
import com.cloudcreativity.cashiersystem.fragments.personal.ChangePwdFragment;
import com.cloudcreativity.cashiersystem.fragments.personal.LogFragment;
import com.cloudcreativity.cashiersystem.fragments.personal.PersonalFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class PersonalIndexModel extends BaseModel<FragmentActivity, FragmentPersonalIndexBinding> {

    private FragmentManager manager;

    PersonalIndexModel(FragmentActivity context, FragmentPersonalIndexBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        onDefault();
    }

    void onDefault(){
        FGUtils.replace(manager, R.id.framePersonal,new PersonalFragment());
    }

    void onChangePwd() {
        FGUtils.replace(manager,R.id.framePersonal,new ChangePwdFragment());
    }
    void onChangeMobile() {
        FGUtils.replace(manager,R.id.framePersonal,new ChangeMobileFragment());
    }

    void onLog(){
        FGUtils.replace(manager,R.id.framePersonal,new LogFragment());
    }
}
