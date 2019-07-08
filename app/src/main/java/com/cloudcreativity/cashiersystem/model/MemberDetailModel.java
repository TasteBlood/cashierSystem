package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;

import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberDetailBinding;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

public class MemberDetailModel extends BaseModel<FragmentActivity, FragmentMemberDetailBinding> {

    public MemberDetailModel(FragmentActivity context, FragmentMemberDetailBinding binding) {
        super(context, binding);
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_LIST);
    }
}
