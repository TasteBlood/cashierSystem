package com.cloudcreativity.cashiersystem.model;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderDetailBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOrderGoodsBinding;
import com.cloudcreativity.cashiersystem.entity.OrderDetailEntity;
import com.cloudcreativity.cashiersystem.entity.OrderItemEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BackGoodsDialogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailModel extends BaseModel<FragmentActivity, FragmentOrderDetailBinding> {

    private OrderDetailEntity detailEntity;
    public BaseBindingRecyclerViewAdapter<OrderItemEntity, ItemLayoutOrderGoodsBinding> adapter;

    public OrderDetailModel(FragmentActivity context, FragmentOrderDetailBinding binding) {
        super(context, binding);
        adapter = new BaseBindingRecyclerViewAdapter<OrderItemEntity, ItemLayoutOrderGoodsBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_order_goods;
            }

            @Override
            protected void onBindItem(ItemLayoutOrderGoodsBinding binding, OrderItemEntity item, int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#f5efef"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));
                }

                binding.tvBackGoods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new BackGoodsDialogUtils().show(OrderDetailModel.this.context, 10, 10.0, new BackGoodsDialogUtils.OnOkListener() {
                            @Override
                            public void onOk(double num, double money) {

                            }
                        });
                    }
                });
            }
        };
        binding.rcvOrderList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        loadData();
    }

    private void loadData(){
        detailEntity = new OrderDetailEntity();
        detailEntity.setCreateTime("2019-02-12 23:45:00");
        detailEntity.setDiscountMoney(129.00);
        detailEntity.setTotalMoney(130.00);
        detailEntity.setIdentity("VIP会员");
        detailEntity.setMemberId(620101001L);
        detailEntity.setOrderNum("6201011000119021210001");
        detailEntity.setPawWay("1");
        detailEntity.setRemark("送309");
        detailEntity.setState(0);

        List<OrderItemEntity> entities = new ArrayList<>();
        for(int i=0;i<=20;i++){
            OrderItemEntity entity = new OrderItemEntity();
            entity.setId(6231001);
            entity.setMoney(23.00);
            entity.setPrice(12.00);
            entity.setName("高原咸菜");
            entity.setUnit("斤");
            entity.setNum(10);
            entities.add(entity);
        }

        detailEntity.setItems(entities);

        adapter.getItems().addAll(detailEntity.getItems());
    }

    //回退页面
    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_LIST);
    }
}
