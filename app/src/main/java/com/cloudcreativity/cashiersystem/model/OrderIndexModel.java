package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderIndexBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.OrderListFragment;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

public class OrderIndexModel extends BaseModel<FragmentActivity, FragmentOrderIndexBinding> {

    private FragmentManager manager;

    public OrderIndexModel(FragmentActivity context, FragmentOrderIndexBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        onDefault();
    }

    void onDefault(){
        FGUtils.replaceNoAnim(manager, R.id.frameOrder,new OrderListFragment());
    }

    void onDetail(){

    }
}