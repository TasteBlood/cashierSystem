package com.cloudcreativity.cashiersystem.fragments;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.CashierFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OpenOrderFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderIndexFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderListFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class CashierIndexModel extends BaseModel<FragmentActivity, FragmentCashierIndexBinding>{

    private FragmentManager fragmentManager;

    private CashierFragment cashierFragment;
    private OpenOrderFragment openOrderFragment;
    private OrderIndexFragment orderListFragment;

    CashierIndexModel(FragmentActivity context, FragmentCashierIndexBinding binding) {
        super(context, binding);

        fragmentManager = context.getSupportFragmentManager();
        cashierFragment = new CashierFragment();
        orderListFragment = new OrderIndexFragment();
        openOrderFragment = new OpenOrderFragment();
        onDefault();
    }


    void onQuery(){
        if(fragmentManager.findFragmentByTag("orderIndex")!=null){
            fragmentManager.beginTransaction()
                    .hide(cashierFragment)
                    .hide(openOrderFragment)
                    .show(orderListFragment)
                    .commit();
        }else{

            fragmentManager.beginTransaction()
                    .hide(openOrderFragment)
                    .hide(cashierFragment)
                    .add(R.id.frameCashier,orderListFragment,"orderIndex")
                    .show(orderListFragment)
                    .commit();
        }
//        orderListFragment = new OrderIndexFragment();
//        fragmentManager.beginTransaction()
//                    .hide(cashierFragment)
//                    .hide(openOrderFragment)
//                    .add(R.id.frameCashier,orderListFragment,"orderIndex")
//                    .show(orderListFragment)
//                    .commit();
    }

    void onOpen(){
        //清空上次开单的信息
        if(openOrderFragment.getModel()!=null)
            openOrderFragment.getModel().clear();
        //FGUtils.replace(fragmentManager,R.id.frameCashier,new OpenOrderFragment());
        if(fragmentManager.findFragmentByTag("openOrder")!=null){
            fragmentManager.beginTransaction()
                    .hide(cashierFragment)
                    .hide(orderListFragment)
                    .show(openOrderFragment)
                    .commit();
        }else{

            fragmentManager.beginTransaction()
                    .hide(openOrderFragment)
                    .hide(orderListFragment)
                    .add(R.id.frameCashier,openOrderFragment,"openOrder")
                    .show(openOrderFragment)
                    .commit();
        }
    }

    void onDefault() {
        if(fragmentManager.findFragmentByTag("cashier")!=null){
            fragmentManager.beginTransaction()
                    .hide(orderListFragment)
                    .hide(openOrderFragment)
                    .show(cashierFragment)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .hide(openOrderFragment)
                    .hide(orderListFragment)
                    .add(R.id.frameCashier,cashierFragment,"cashier")
                    .show(cashierFragment)
                    .commit();
        }
    }
}
