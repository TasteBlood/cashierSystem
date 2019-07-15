package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOrderDetailBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOrderGoodsBinding;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.entity.OrderDetailEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.BackGoodsDialogUtils;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailModel extends BaseModel<FragmentActivity, FragmentOrderDetailBinding> {

    public ObservableField<OrderDetailEntity> detailEntity = new ObservableField<>();
    private BaseDialogImpl baseDialog;
    public BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOrderGoodsBinding> adapter;

    public OrderDetailModel(FragmentActivity context, FragmentOrderDetailBinding binding,BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        adapter = new BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOrderGoodsBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_order_goods;
            }

            @Override
            protected void onBindItem(ItemLayoutOrderGoodsBinding binding, OpenOrderGoodsEntity item, int position) {
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
        HttpUtils.getInstance().queryOrder(BaseApp.CURRENT_OID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        OrderDetailEntity order = new Gson().fromJson(t, OrderDetailEntity.class);
                        if(order==null|| TextUtils.isEmpty(order.getOrderNo())){
                            ToastUtils.showShortToast(context,"订单查询失败，请重试");
                            return;
                        }
                        detailEntity.set(order);
                        adapter.getItems().addAll(order.getOrderDetails());
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    //回退页面
    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_ORDER_LIST);
        BaseApp.CURRENT_OID = 0;
    }
}
