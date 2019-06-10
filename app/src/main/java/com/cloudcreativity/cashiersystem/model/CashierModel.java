package com.cloudcreativity.cashiersystem.model;

import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

public class CashierModel {

    public void onQueryClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER);
    }

    public void onOpenClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_OPEN_ORDER);
    }
}
