package com.cloudcreativity.cashiersystem.model;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderListBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOrderBinding;
import com.cloudcreativity.cashiersystem.entity.OrderEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OrderListModel extends BaseModel<FragmentActivity, FragmentOrderListBinding> {

    private BaseDialogImpl baseDialog;

    public BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding> adapter;

    public OrderListModel(FragmentActivity context, FragmentOrderListBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        binding.rcvOrderList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        loadData();
    }

    private void loadData(){
        adapter = new BaseBindingRecyclerViewAdapter<OrderEntity, ItemLayoutOrderBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_order;
            }

            @Override
            protected void onBindItem(ItemLayoutOrderBinding binding, OrderEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };

        List<OrderEntity> entities = new ArrayList<>();
        for(int i=0;i<=20;i++){
            OrderEntity entity = new OrderEntity();
            entity.setId("620101100119061110001");
            entity.setMoney(20000.0);
            entity.setCreateTime("2019-06-11 12:30:00");
            entity.setPayway("1");
            entity.setState(0);
            entities.add(entity);
        }

        adapter.getItems().addAll(entities);
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER);
    }
}
