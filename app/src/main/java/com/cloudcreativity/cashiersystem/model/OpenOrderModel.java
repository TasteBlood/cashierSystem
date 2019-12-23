package com.cloudcreativity.cashiersystem.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseBindingRecyclerViewAdapter;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentOpenOrderBinding;
import com.cloudcreativity.cashiersystem.databinding.ItemLayoutOpenOrderBinding;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;
import com.cloudcreativity.cashiersystem.entity.ListEntity;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.fragments.cashier.GoodsFragment;
import com.cloudcreativity.cashiersystem.fragments.cashier.ListOrderFragment;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.CallDialogUtils;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.FGUtils;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.InputDialogUtils;
import com.cloudcreativity.cashiersystem.utils.OpenOrderFinalDialog;
import com.cloudcreativity.cashiersystem.utils.OrderDao;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OpenOrderModel extends BaseModel<FragmentActivity, FragmentOpenOrderBinding> {

    public BaseBindingRecyclerViewAdapter<OpenOrderGoodsEntity, ItemLayoutOpenOrderBinding> adapter;


    private double totalMoney;
    private double discountTotal;
    private double finalMoney;
    public ObservableField<MemberEntity> member = new ObservableField<>();
    public ObservableField<String> remark = new ObservableField<>();
    private BaseDialogImpl baseDialog;
    private OpenOrderFinalDialog finalDialog;

    private ListOrderFragment orderFragment;
    private GoodsFragment goodsFragment;
    private ListEntity entity;
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
                            public void onOk(double number) {
                                number = StrUtils.get2BitDecimal(number);
                                //设置数量
                                if (item.getStock() < number) {
                                    ToastUtils.showShortToast(context, "已超过库存");
                                    return;
                                }
                                item.setAmount(number);
                                adapter.notifyItemChanged(position);
                                refreshState();
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

        orderFragment = ListOrderFragment.getInstance("open");
        goodsFragment = new GoodsFragment();
    }

    public void initialize() {
        //加载默认的商品fragment
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameGoods2, new GoodsFragment())
                .commit();
    }

    public void openList(){
        changeFragment("listInOpen");
    }

    public void fillOrder(final ListEntity data){
        if(data==null) return;
        if(adapter.getItemCount()>0) {
            new AlertDialog.Builder(context)
                    .setMessage("当前有订单未结，确定覆盖吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenOrderModel.this.entity = data;
                            member.set(null);
                            //如果有会员，先加载会员信息
                            if(!TextUtils.isEmpty(entity.getMobile())){
                                queryMember(entity.getMobile());
                            }
                            //设置备注
                            if(!TextUtils.isEmpty(entity.getMemo())){
                                OpenOrderModel.this.remark.set(entity.getMemo());
                            }
                            //加载商品
                            adapter.getItems().clear();
                            adapter.getItems().addAll(entity.getItems());
                            refreshState();
                            calculateTotal();
                        }
                    }).show();
        }else{
            this.entity = data;
            //如果有会员，先加载会员信息
            member.set(null);
            if(!TextUtils.isEmpty(entity.getMobile())){
                queryMember(entity.getMobile());
            }
            //设置备注
            if(!TextUtils.isEmpty(entity.getMemo())){
                this.remark.set(entity.getMemo());
            }
            //加载商品
            adapter.getItems().clear();
            adapter.getItems().addAll(entity.getItems());
            refreshState();
            calculateTotal();
        }
    }

    public void changeFragment(String name){
        if("goodsInOpen".equals(name)){
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
            FGUtils.replaceNoAnim(context.getSupportFragmentManager(),R.id.frameGoods2,goodsFragment);
        }else if("listInOpen".equals(name)){
            //展示挂单
            FGUtils.replaceNoAnim(context.getSupportFragmentManager(),R.id.frameGoods2,orderFragment);
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

    public void onBack() {
        deleteOrder();
        //firstly saved data into database
        if(adapter.getItemCount()>0){
            // has order item data
            ListEntity entity;
            if(member.get()!=null){
                entity = new ListEntity(member.get().getMobile(),
                        member.get().getId(),
                        TextUtils.isEmpty(remark.get())?"":remark.get(),
                        totalMoney,discountTotal,totalMoney-discountTotal,adapter.getItems());
            }else{
                entity = new ListEntity("",
                        0,
                        TextUtils.isEmpty(remark.get())?"":remark.get(),
                        totalMoney,discountTotal,totalMoney-discountTotal,adapter.getItems());
            }
            OrderDao.getInstance(BaseApp.app).addOrder(entity);
        }

        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_CASHIER);
        member.set(null);
        clear();
    }

    public void onSubmitClick() {
        calculateTotalFinal();
        List<OpenOrderGoodsEntity> entities = new ArrayList<>();
        float amount = 0;
        for (OpenOrderGoodsEntity entity : adapter.getItems()) {
            amount += entity.getAmount();
            entities.add(entity);
        }
        finalDialog = new OpenOrderFinalDialog(totalMoney, discountTotal, amount, member.get(), remark.get(), entities);
        finalDialog.show(context, baseDialog);
    }

    public void onEditClick() {
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
            double totalTemp = entity.getAmount() * entity.getPrice();
            totalMoney += totalTemp;
            if(entity.getDiscount()<=0){
                discountTotal += (totalTemp - totalTemp);
            }else {
                discountTotal += (totalTemp - (totalTemp * (entity.getDiscount() / 100)));
            }
            entity.calculateMoney();
        }
        binding.tvTotal.setText("￥" + StrUtils.get2BitDecimal(totalMoney / 100));
        binding.tvDiscount.setText("折扣终价(-" + StrUtils.get2BitDecimal(discountTotal / 100) + ")");
        finalMoney = totalMoney - discountTotal;
        binding.tvDiscountMoney.setText("￥" + (StrUtils.get2BitDecimal(finalMoney / 100)));
    }

    /**
     * 添加商品
     * @param goodsEntity 商品内容
     */
    public void pushGoods(final GoodsEntity goodsEntity) {
        new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
            @Override
            public void onOk(double number) {
                number = StrUtils.get2BitDecimal(number);
                for (int i = 0; i < adapter.getItems().size(); i++) {
                    if (adapter.getItems().get(i).getGoodsId() == goodsEntity.getGoodsId()) {
                        if (adapter.getItems().get(i).getAmount() > adapter.getItems().get(i).getStock()) {
                            ToastUtils.showShortToast(context, "已超过库存");
                            return;
                        }else{
                            if(adapter.getItems().get(i).getAmount() + number <= adapter.getItems().get(i).getStock()){
                                adapter.getItems().get(i).setAmount(adapter.getItems().get(i).getAmount() + number);
                            }else{
                                ToastUtils.showShortToast(context, "已超过库存");
                                return;
                            }
                        }
                        calculateTotal();
                        adapter.notifyItemChanged(i);
                        return;
                    }
                }

                // 新商品
                if(number <= goodsEntity.getComputerStock()){
                    OpenOrderGoodsEntity entity = new OpenOrderGoodsEntity();
                    entity.setDiscount(goodsEntity.getRate());
                    entity.setCategoryOneId(goodsEntity.getCategoryOneId());
                    entity.setGoodsId(goodsEntity.getGoodsId());
                    entity.setStock(goodsEntity.getComputerStock());
                    entity.setGoodsName(goodsEntity.getGoodsDomain().getName());
                    entity.setAmount(number);
                    entity.setStandards(goodsEntity.getGoodsDomain().getStandards());
                    entity.setPrice(goodsEntity.getGoodsDomain().getPrice());
                    entity.setUnit(goodsEntity.getGoodsDomain().getUnit());
                    adapter.getItems().add(entity);
                    refreshState();
                    calculateTotal();
                }else{
                    ToastUtils.showShortToast(context, "已超过库存");
                }


            }
        },goodsEntity.getGoodsDomain().getUnit(),goodsEntity.getGoodsName());

        //new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
        //@Override
        //public void onOk(float number)
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
        deleteOrder();
        this.adapter.getItems().clear();
        totalMoney = 0;
        finalMoney = 0;
        discountTotal = 0;
        member.set(null);
        refreshState();
        binding.tvTotal.setText("￥0.0");
        binding.tvDiscount.setText("折扣终价(-0.0)");
        finalMoney = totalMoney - discountTotal;
        remark.set(null);
        binding.tvDiscountMoney.setText("￥0.0");
        this.entity = null;
    }

    private void deleteOrder(){
        //删除挂单信息
        if(this.entity!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OrderDao.getInstance(BaseApp.app).deleteOrder(entity.getId());
                }
            }).start();
        }
    }

    //计算总价
    private void calculateTotal() {
        totalMoney = 0;
        discountTotal = 0;
        finalMoney = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            OpenOrderGoodsEntity entity = adapter.getItems().get(i);
            double totalTemp = entity.getAmount() * entity.getPrice();
            totalMoney += totalTemp;
            //这一步就是，当前必须是会员并且有折扣才会打折
            if(entity.getDiscount()<=0 || member.get()==null){
                discountTotal += (totalTemp - totalTemp);
            }else {
                discountTotal += (totalTemp - (totalTemp * (entity.getDiscount() / 100)));
            }

            if(member.get()!=null){
                entity.setMem(true);
            }else{
                entity.setMem(false);
            }

            entity.calculateMoney();
        }
        binding.tvTotal.setText("￥" + StrUtils.get2BitDecimal(totalMoney / 100));
        binding.tvDiscount.setText("折扣终价(-" + StrUtils.get2BitDecimal(discountTotal / 100) + ")");
        finalMoney = totalMoney - discountTotal;
        binding.tvDiscountMoney.setText("￥" + (StrUtils.get2BitDecimal(finalMoney / 100)));
        adapter.notifyDataSetChanged();
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
                calculateTotal();
                return;
            }
            if (!StrUtils.isPhone(s.toString())) {
                member.set(null);
                calculateTotal();
                return;
            }
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
                            calculateTotal();
                            refreshState();
                            return;
                        }
                        member.set(entity);
                        calculateTotal();
                        refreshState();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
