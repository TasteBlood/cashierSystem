package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOpenOrderBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOpenOrderBinding;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.fragments.cashier.GoodsFragment;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.CallDialogUtils;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.InputDialogUtils;
import com.cloudcreativity.cashiersystem.utils.OpenOrderFinalDialog;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OpenOrderModel extends BaseModel<FragmentActivity, FragmentOpenOrderBinding> {

    public BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOpenOrderBinding> adapter;


    private int totalMoney;
    private int discountTotal;
    private int finalMoney;
    public ObservableField<MemberEntity> member = new ObservableField<>();
    public ObservableField<String> remark = new ObservableField<>();
    private BaseDialogImpl baseDialog;

    public OpenOrderModel(FragmentActivity context, FragmentOpenOrderBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        adapter = new BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOpenOrderBinding>(context) {
            @Override
            protected int getLayoutResId(int viewType) {
                return R.layout.item_layout_open_order;
            }

            @Override
            protected void onBindItem(ItemLayoutOpenOrderBinding binding, final OpenOrderGoodsEntity item, final int position) {
                binding.setItem(item);
                if (position % 2 == 1) {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#e1e1e1"));
                } else {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                binding.tvNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
                            @Override
                            public void onOk(float number) {
                                //设置数量
                                item.setAmount(number);
                                adapter.notifyItemChanged(position);
                                calculateTotal();
                            }
                        }, item.getUnit(), item.getGoodsName());
                    }
                });

                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.notifyItemRangeRemoved(position, 1);
                        adapter.getItems().remove(position);
                        refreshState();
                        calculateTotal();
                    }
                });
            }
        };

        binding.rcvOpenOrder.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        refreshState();
        calculateTotal();
    }

    public void initialize() {
        //加载默认的商品fragment
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameGoods2, new GoodsFragment())
                .commit();
    }

    public void onBack() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER);
        clear();
        member.set(null);
    }

    public void onSubmitClick() {
        calculateTotalFinal();
        List<OpenOrderGoodsEntity> entities = new ArrayList<>();
        float amount = 0;
        for(OpenOrderGoodsEntity entity:adapter.getItems()){
            amount += entity.getAmount();
            entities.add(entity);
        }
        new OpenOrderFinalDialog(totalMoney,discountTotal,amount,member.get(),remark.get(),entities).show(context,baseDialog);
    }

    public void onEditClick(){
        new InputDialogUtils().show(context, remark.get(), new InputDialogUtils.OnOkListener() {
            @Override
            public void onOk(String content) {
                remark.set(content);
            }
        });
    }

    private void calculateTotalFinal() {
        totalMoney = 0;
        discountTotal = 0;
        finalMoney = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            OpenOrderGoodsEntity entity = adapter.getItems().get(i);
            totalMoney += entity.getAmount() * entity.getPrice();
            discountTotal += entity.getAmount() * entity.getPrice() * entity.getDiscount();
            entity.calculateMoney();
        }
        binding.tvTotal.setText("￥" + StrUtils.get2BitDecimal(totalMoney/100f));
        binding.tvDiscount.setText("折扣终价(-" + StrUtils.get2BitDecimal(discountTotal/100f) + ")");
        finalMoney = totalMoney - discountTotal;
        binding.tvDiscountMoney.setText("￥" + (StrUtils.get2BitDecimal(finalMoney/100f)));
    }

    /**
     * 添加商品
     *
     * @param goodsEntity 商品内容
     */
    public void pushGoods(final GoodsEntity goodsEntity) {
        for (int i = 0; i < adapter.getItems().size(); i++) {
            if (adapter.getItems().get(i).getGoodsId() == goodsEntity.getGoodsId()) {
                adapter.getItems().get(i).setAmount(adapter.getItems().get(i).getAmount() + 1);
                calculateTotal();
                adapter.notifyItemChanged(i);
                return;
            }
        }
        //new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
        //@Override
        //public void onOk(float number) {
        OpenOrderGoodsEntity entity = new OpenOrderGoodsEntity();
        entity.setDiscount(0);
        entity.setCategoryOneId(goodsEntity.getCategoryOneId());
        entity.setGoodsId(goodsEntity.getGoodsId());
        entity.setGoodsName(goodsEntity.getGoodsDomain().getName());
        entity.setAmount(1);
        entity.setStandards(goodsEntity.getGoodsDomain().getStandards());
        entity.setPrice(goodsEntity.getGoodsDomain().getPrice());
        entity.setUnit(goodsEntity.getGoodsDomain().getUnit());
        adapter.getItems().add(entity);
        refreshState();
        calculateTotal();
        //}
        //},goodsEntity.getGoodsDomain().getUnit(),goodsEntity.getGoodsDomain().getName());

    }

    private void refreshState() {
        if (adapter.getItems() != null && adapter.getItems().size() > 0) {
            binding.btnCheck.setEnabled(true);
        } else {
            binding.btnCheck.setEnabled(false);
        }
    }

    public void clear() {
        this.adapter.getItems().clear();
        totalMoney = 0;
        finalMoney = 0;
        discountTotal = 0;
        refreshState();
        binding.tvTotal.setText("￥0.0");
        binding.tvDiscount.setText("折扣终价(-0.0)");
        finalMoney = totalMoney - discountTotal;
        remark.set(null);
        binding.tvDiscountMoney.setText("￥0.0");
    }

    //计算总价
    private void calculateTotal() {
        totalMoney = 0;
        discountTotal = 0;
        finalMoney = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            OpenOrderGoodsEntity entity = adapter.getItems().get(i);
            totalMoney += entity.getAmount() * entity.getPrice();
            discountTotal += entity.getAmount() * entity.getPrice() * entity.getDiscount();
        }
        binding.tvTotal.setText("￥" + StrUtils.get2BitDecimal(totalMoney/100f));
        binding.tvDiscount.setText("折扣终价(-" + StrUtils.get2BitDecimal(discountTotal/100f) + ")");
        finalMoney = totalMoney - discountTotal;
        binding.tvDiscountMoney.setText("￥" + (StrUtils.get2BitDecimal(finalMoney/100f)));
    }

    //根据barCode查询商品
    public void queryGoods(String code) {
        HttpUtils.getInstance().queryGoods(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, true) {
                    @Override
                    public void onSuccess(String t) {
                        GoodsEntity entity = new Gson().fromJson(t, GoodsEntity.class);
                        if (entity == null || entity.getId() == 0) {
                            ToastUtils.showShortToast(context, "无该商品");
                            return;
                        }
                        pushGoods(entity);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
            if (TextUtils.isEmpty(s.toString())) {
                member.set(null);
                return;
            }
            if (!StrUtils.isPhone(s.toString()))
                return;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryMember(s.toString());
                }
            }, 500);
        }
    };

    private void queryMember(String phone) {
        HttpUtils.getInstance().queryMemberByPhone(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, true) {
                    @Override
                    public void onSuccess(String t) {
                        MemberEntity entity = new Gson().fromJson(t, MemberEntity.class);
                        if (entity == null || entity.getId() <= 0) {
                            ToastUtils.showShortToast(context, "暂无会员信息");
                            return;
                        }
                        member.set(entity);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }


}
