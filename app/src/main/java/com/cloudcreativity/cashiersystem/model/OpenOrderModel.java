package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOpenOrderBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOpenOrderBinding;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.fragments.cashier.GoodsFragment;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.CallDialogUtils;
import com.cloudcreativity.cashiersystem.utils.OpenOrderFinalDialog;
import com.cloudcreativity.cashiersystem.utils.StrUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OpenOrderModel extends BaseModel<FragmentActivity, FragmentOpenOrderBinding> {

    public BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOpenOrderBinding> adapter;

    public float total = 0.0f;
    private float discount = 0.0f;
    public ObservableField<String> remark = new ObservableField<>();

    public OpenOrderModel(FragmentActivity context, FragmentOpenOrderBinding binding) {
        super(context, binding);
        adapter = new BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOpenOrderBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_open_order;
            }

            @Override
            protected void onBindItem(ItemLayoutOpenOrderBinding binding, final OpenOrderGoodsEntity item, final int position) {
                binding.setItem(item);
                if(position%2==1){
                    binding.getRoot().setBackgroundColor(Color.parseColor("#e1e1e1"));
                }else{
                    binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                binding.tvNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
                            @Override
                            public void onOk(float number) {
                                //设置数量
                                item.setNumber(number);
                                adapter.notifyItemChanged(position);
                                calculateTotal();
                            }
                        },item.getUnit());
                    }
                });
            }
        };

        binding.rcvOpenOrder.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        refreshState();
    }

    public void initialize(){
        //加载默认的商品fragment
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameGoods2,new GoodsFragment())
                .commit();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER);
        clear();
    }

    public void onSubmitClick(){
        new OpenOrderFinalDialog().show(context);
    }

    /**
     * 添加商品
     * @param goodsEntity 商品内容
     */
    public void pushGoods(GoodsEntity goodsEntity) {
        for(int i=0;i<adapter.getItems().size();i++){
            if(adapter.getItems().get(i).getGid()==goodsEntity.getGoodsId()){
                adapter.getItems().get(i).setNumber(adapter.getItems().get(i).getNumber()+1);
                calculateTotal();
                adapter.notifyItemChanged(i);
                return;
            }
        }
        OpenOrderGoodsEntity entity = new OpenOrderGoodsEntity();
        entity.setDiscount(0);
        entity.setGid(goodsEntity.getGoodsId());
        entity.setName(goodsEntity.getGoodsDomain().getName());
        entity.setNumber(0);
        entity.setPrice(goodsEntity.getGoodsDomain().getPrice());
        entity.setUnit(goodsEntity.getGoodsDomain().getUnit());
        adapter.getItems().add(entity);
        refreshState();
    }

    private void refreshState(){
        if(adapter.getItems()!=null&&adapter.getItems().size()>0){
            binding.btnCheck.setEnabled(true);
        }else{
            binding.btnCheck.setEnabled(false);
        }
    }

    public void clear(){
        this.adapter.getItems().clear();
        total = 0.0f;
        discount = 0.0f;
        refreshState();
    }

    //计算总价
    private void calculateTotal(){
        for(int i=0;i<adapter.getItemCount();i++){
            OpenOrderGoodsEntity entity = adapter.getItems().get(i);
            total += entity.getNumber()*entity.getPrice()/100;
        }
        binding.tvTotal.setText("￥"+ total);
        binding.tvDiscount.setText("折扣终价(-"+discount+")");
        binding.tvDiscountMoney.setText(discount>0?("￥"+total*discount):("￥"+ total));
        total = 0.0f;
    }
}
