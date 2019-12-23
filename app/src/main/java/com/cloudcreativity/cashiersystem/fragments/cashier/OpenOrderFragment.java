package com.cloudcreativity.cashiersystem.fragments.cashier;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.LazyFragment;
import com.cloudcreativity.cashiersystem.databinding.FragmentOpenOrderBinding;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.entity.ListEntity;
import com.cloudcreativity.cashiersystem.model.OpenOrderModel;
import com.cloudcreativity.cashiersystem.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class OpenOrderFragment extends LazyFragment {

    private OpenOrderModel model;


    public OpenOrderModel getModel() {
        return model;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOpenOrderBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_open_order, container, false);
        model = new OpenOrderModel(context, binding,this);
        binding.setModel(model);
        model.initialize();
        binding.getRoot().setClickable(true);
        return binding.getRoot();
    }

    @Override
    public void initialLoadData() {
    }

    @Subscribe
    public void onEvent(GoodsEntity goodsEntity){
        model.pushGoods(goodsEntity);
    }

    @Subscribe
    public void onEvent(ListEntity entity){
        LogUtils.e("xuxiwu","fill order once");
        if(entity==null) return;
        //开始填充数据
        if(model!=null)
            model.fillOrder(entity);
    }

    @Subscribe
    public void onEvent(String msg){
        if("list_order_close".equals(msg)){
            if(getUserVisibleHint()){
                if(model!=null){
                    model.changeFragment("goodsInOpen");
                }
            }
        }
    }

    /**
     * 所有的code相关的操作都必须这么做
     * 1、 name: goodsCode code:xxxxxxxxx
     * 2、 name: payCode code:xxxxxxxxx
     */
    @Subscribe
    public void onEvent(Map<String,String> map){
        if(model==null)
            return;
        //根据barCode查询商品
        if("goodsCode".equals(map.get("name"))){
            //获取code
            model.queryGoods(map.get("code"));
        }else if("clear_order".equals(map.get("name"))){
            model.clear();
        }
    }

}
