package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentCashierBinding;
import com.cloudcreativity.cashiersystem.fragments.cashier.GoodsFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.ListOrderFragment;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.FGUtils;

import org.greenrobot.eventbus.EventBus;

public class CashierModel extends BaseModel<FragmentActivity, FragmentCashierBinding> {


    private FragmentManager manager;

    private GoodsFragment goodsFragment;
    private ListOrderFragment orderFragment;

    public GoodsFragment getGoodsFragment() {
        return goodsFragment;
    }

    public ListOrderFragment getOrderFragment() {
        return orderFragment;
    }

    public CashierModel(FragmentActivity context, FragmentCashierBinding binding) {
        super(context, binding);
        manager = context.getSupportFragmentManager();
        goodsFragment = new GoodsFragment();
        orderFragment = ListOrderFragment.getInstance("cashier");
    }

    public void initialize(){
        //加载默认的商品fragment
//        context.getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameGoods1,new GoodsFragment())
//                .commit();
//        FGUtils.replaceNoAnim(context.getSupportFragmentManager(),
//                R.id.frameGoods1,new GoodsFragment());
        changeFragment("goodsInCashier");
    }

    public void changeFragment(String name){
        if("goodsInCashier".equals(name)){
            //展示商品
//            if(manager.findFragmentByTag("goodsInCashier")!=null){
//                manager.beginTransaction()
//                        .hide(orderFragment)
//                        .show(goodsFragment)
//                        .commit();
//            }else{
//                manager.beginTransaction()
//                        .hide(orderFragment)
//                        .add(R.id.frameGoods1,goodsFragment,"goodsInCashier")
//                        .commit();
//            }
            FGUtils.replaceNoAnim(manager,R.id.frameGoods1,goodsFragment);
        }else if("listInCashier".equals(name)){
            //展示挂单
            FGUtils.replaceNoAnim(manager,R.id.frameGoods1,orderFragment);
//            if(manager.findFragmentByTag("listInCashier")!=null){
//                manager.beginTransaction()
//                        .hide(goodsFragment)
//                        .show(orderFragment)
//                        .commit();
//            }else{
//                manager.beginTransaction()
//                        .add(R.id.frameGoods1,orderFragment,"listInCashier")
//                        .hide(goodsFragment)
//                        .commit();
//            }
        }
    }

    public void onQueryClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER);
    }

    public void onListOrderClick(){
//        FGUtils.replaceNoAnim(context.getSupportFragmentManager(),
//                R.id.frameGoods1,new ListOrderFragment());
        changeFragment("listInCashier");
    }

    public void onOpenClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_OPEN_ORDER);
    }

}
