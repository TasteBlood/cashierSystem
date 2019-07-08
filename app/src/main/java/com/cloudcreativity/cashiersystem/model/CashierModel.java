package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.GoodsFragment;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

public class CashierModel extends BaseModel<FragmentActivity, FragmentCashierBinding> {

    public CashierModel(FragmentActivity context, FragmentCashierBinding binding) {
        super(context, binding);

    }

    public void initialize(){
        //加载默认的商品fragment
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameGoods1,new GoodsFragment())
                .commit();
    }

    public void onQueryClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER);
    }

    public void onOpenClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_OPEN_ORDER);
    }
}
