package com.cloudcreativity.cashiersystem.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.CashierFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderDetailFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderIndexFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class CashierIndexModel extends BaseModel<FragmentActivity, FragmentCashierIndexBinding>{

    private FragmentManager fragmentManager;

    CashierIndexModel(FragmentActivity context, FragmentCashierIndexBinding binding) {
        super(context, binding);

        fragmentManager = context.getSupportFragmentManager();
        FGUtils.replace(fragmentManager,R.id.frameCashier,new CashierFragment());
    }

    void onQuery(){
        FGUtils.replace(fragmentManager,R.id.frameCashier,new OrderIndexFragment());
        //fragmentManager.beginTransaction().replace(R.id.frameCashier,new OrderListFragment()).commit();
    }

    void onOpen(){
        FGUtils.replace(fragmentManager,R.id.frameCashier,new OrderDetailFragment());
        //fragmentManager.beginTransaction().replace(R.id.frameCashier,new OrderDetailFragment()).commit();
    }

    void onDefault() {
//        fragmentManager.popBackStackImmediate();
        FGUtils.replace(fragmentManager,R.id.frameCashier,new CashierFragment());
        //fragmentManager.beginTransaction().replace(R.id.frameCashier,new CashierFragment()).commit();
    }
}
