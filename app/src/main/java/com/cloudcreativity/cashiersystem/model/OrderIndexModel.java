package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderDetailFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderListFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class OrderIndexModel extends BaseModel<FragmentActivity, FragmentOrderIndexBinding> {

    private FragmentManager manager;

    private OrderListFragment orderListFragment;
    private OrderDetailFragment orderDetailFragment;
    public OrderIndexModel(FragmentActivity context, FragmentOrderIndexBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        orderDetailFragment = new OrderDetailFragment();
        orderListFragment = new OrderListFragment();
        onDefault();
    }

    public void onDefault(){
        if(manager.findFragmentByTag("orderList")!=null){
            manager.beginTransaction()
                    .show(orderListFragment)
                    .hide(orderDetailFragment)
                    .commit();
        }else{
            manager.beginTransaction()
                    .hide(orderDetailFragment)
                    .add(R.id.frameOrder,orderListFragment,"orderList")
                    .show(orderListFragment)
                    .commit();
        }
    }

    public void onDetail(){
//        if(manager.findFragmentByTag("orderDetail")!=null){
//            manager.beginTransaction()
//                    .show(orderDetailFragment)
//                    .hide(orderListFragment)
//                    .commit();
//        }else{
//            manager.beginTransaction()
//                    .hide(orderListFragment)
//                    .add(R.id.frameOrder,orderDetailFragment,"orderDetail")
//                    .show(orderDetailFragment)
//                    .commit();
//        }
        orderDetailFragment = new OrderDetailFragment();
        manager.beginTransaction()
                .hide(orderListFragment)
                .add(R.id.frameOrder,orderDetailFragment,"orderDerail")
                .show(orderDetailFragment)
                .commit();
    }
}