package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.LayoutOpenOrderFinalBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.entity.OrderEntity;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OpenOrderFinalDialog {
    private Dialog dialog;
    private BaseDialogImpl baseDialog;
    //传递过来的money是分为单位
    //订单总计,未折扣总额
    private double totalMoney;

    //订单折扣数
    private double discountMoney;

    //订单商品总数
    private double amount;

    //订单商品信息
    private List<OpenOrderGoodsEntity> entities;

    private double finalMoney;

    //去零的剩值
    private double zeroMoney;
    //去零的差值
    private double zeroValue;

    private TextView tv_money;
    private TextView tv_zero;
    private LayoutOpenOrderFinalBinding binding;
    private MemberEntity memberEntity;
    private Activity context;
    private String remark;
    private String authCode = null;

    /**
     * @param totalMoney    订单总额
     * @param discountMoney 折扣总计
     * @param amount        商品总数
     * @param memberEntity  会员信息
     * @param entities      商品信息
     */
    public OpenOrderFinalDialog(double totalMoney, double discountMoney, double amount,
                                MemberEntity memberEntity, String remark, List<OpenOrderGoodsEntity> entities) {
        this.remark = remark;
        this.totalMoney = totalMoney;
        this.discountMoney = discountMoney;
        this.entities = entities;
        this.amount = amount;
        this.finalMoney = totalMoney - discountMoney;
        this.memberEntity = memberEntity;
        zeroMoney = this.finalMoney;
        zeroValue = 0;
    }

    public void show(Activity context, BaseDialogImpl baseDialog) {
        this.context = context;
        this.baseDialog = baseDialog;
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_open_order_final, null, false);
        binding.setUtils(this);
        tv_money = binding.getRoot().findViewById(R.id.tv_money);
        tv_money.setText("￥" + StrUtils.get2BitDecimal(finalMoney / 100f));
        tv_zero = binding.getRoot().findViewById(R.id.tv_zero);
        SoftKeyboardUtils.hideSoftInput2(context);
        if (this.memberEntity == null) {
            binding.tvBalance.setText("");
            binding.btnBalance.setEnabled(false);
        } else {
            if (TextUtils.isEmpty(this.memberEntity.getBalance())) {
                binding.tvBalance.setText("");
                binding.btnBalance.setEnabled(false);
            } else {
                int balance = Integer.parseInt(memberEntity.getBalance());
                if (this.finalMoney > balance) {
                    binding.tvBalance.setText("(会员余额:￥" + StrUtils.get2BitDecimal(balance / 100f) + ")");
                    binding.btnBalance.setEnabled(false);
                } else {
                    binding.tvBalance.setText("(会员余额:￥" + StrUtils.get2BitDecimal(balance / 100f) + ")");
                    binding.btnBalance.setEnabled(true);
                }
            }
        }
        dialog.setContentView(binding.getRoot());
//        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.END);
        //window.getAttributes().width = width/3;
        dialog.show();
    }

    public void zeroClick() {
        double money = this.finalMoney;
        if (money % 100 != 0) {
            //说明不是是整元，需要摸0
            double zero = money % 100;
            zeroMoney = money - zero;
            zeroValue = zero;
            this.tv_zero.setText("￥" + StrUtils.get2BitDecimal(zeroMoney / 100f));

            // 去0判断余额够不够
            if (this.memberEntity == null) {
                binding.tvBalance.setText("");
                binding.btnBalance.setEnabled(false);
            } else {
                if (TextUtils.isEmpty(this.memberEntity.getBalance())) {
                    binding.tvBalance.setText("");
                    binding.btnBalance.setEnabled(false);
                } else {
                    int balance = Integer.parseInt(this.memberEntity.getBalance());
                    if (zeroMoney > balance) {
                        binding.tvBalance.setText("(会员余额:￥" + StrUtils.get2BitDecimal(balance / 100f) + ")");
                        binding.btnBalance.setEnabled(false);
                    } else {
                        binding.tvBalance.setText("(会员余额:￥" + StrUtils.get2BitDecimal(balance / 100f) + ")");
                        binding.btnBalance.setEnabled(true);
                    }
                }
            }
        } else {
            //整元不需要
            tv_zero.setText("￥" + StrUtils.get2BitDecimal(this.finalMoney / 100f));
        }
    }

    public void onBack() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void onMobileClick() {
        //开始等待扫码回掉
        ScanCodeDialog scanCodeDialog = new ScanCodeDialog(context, R.style.myProgressDialogStyle);
        scanCodeDialog.setOnOkListener(new ScanCodeDialog.OnOkListener() {
            @Override
            public void onOk(String code) {
                long mid = 0;
                String mobile = null;
                if (memberEntity != null) {
                    mid = memberEntity.getId();
                    mobile = memberEntity.getMobile();
                }
                binding.layoutPay.setVisibility(View.VISIBLE);
                submitOrder(Double.valueOf(totalMoney).intValue(),
                        Double.valueOf(finalMoney).intValue(), Double.valueOf(zeroMoney).intValue(), Double.valueOf(discountMoney).intValue(), 0, Double.valueOf(zeroValue).intValue(), mid, mobile,
                        AppConfig.PAY_WAY.PAY_MOBILE, 1, remark, entities, code);
            }
        });
        scanCodeDialog.show();
    }

    public void onMoneyClick() {
        binding.layoutPay.setVisibility(View.VISIBLE);
        USBUtils.getInstance(context).openCashBox();
        long mid = 0;
        String mobile = null;
        if (memberEntity != null) {
            mid = memberEntity.getId();
            mobile = memberEntity.getMobile();
        }
        submitOrder(Double.valueOf(totalMoney).intValue(),
                Double.valueOf(finalMoney).intValue(),
                Double.valueOf(zeroMoney).intValue(),
                Double.valueOf(discountMoney).intValue(), 0, Double.valueOf(zeroValue).intValue(), mid, mobile,
                AppConfig.PAY_WAY.PAY_MONEY, 1, remark, entities, authCode);
    }

    public void onBalanceClick() {
        binding.layoutPay.setVisibility(View.VISIBLE);
        //binding.layoutPay.setVisibility(View.VISIBLE);
        long mid = 0;
        String mobile = null;
        if (memberEntity != null) {
            mid = memberEntity.getId();
            mobile = memberEntity.getMobile();
        }
        submitOrder(Double.valueOf(totalMoney).intValue(),
                Double.valueOf(finalMoney).intValue(), Double.valueOf(zeroMoney).intValue(),
                Double.valueOf(discountMoney).intValue(), 0, Double.valueOf(zeroValue).intValue(), mid, mobile,
                AppConfig.PAY_WAY.PAY_BALANCE, 1, remark, entities, authCode);
    }


    private void submitOrder(final int totalMoney, int finalMoney, final int payMoney, final int discountMoney,
                             final int integralMoney, final int zeroMoney, long memberId, String mobile,
                             final int payType, final int type, final String remark, final List<OpenOrderGoodsEntity> goods,
                             String code) {
        binding.pbPay.setVisibility(View.VISIBLE);
        binding.tvResult.setText("支付中，请稍后...");
        StringBuilder builder = new StringBuilder();
        builder.append("totalMoney=").append(totalMoney).append("\n");
        builder.append("finalMoney=").append(finalMoney).append("\n");
        builder.append("discountMoney=").append(discountMoney).append("\n");
        builder.append("zeroMoney=").append(zeroMoney).append("\n");
        builder.append("integralMoney=").append(integralMoney).append("\n");
        builder.append("payMoney=").append(payMoney).append("\n");

        LogUtils.e("xuxiwu", builder.toString());
        //开始组装参数
        HttpUtils.getInstance().submitOrder(discountMoney, integralMoney, memberId, mobile,
                payMoney, payType, totalMoney, finalMoney, type, zeroMoney, remark, new Gson().toJson(goods), code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        //将goods对应的库存都更新了
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(OpenOrderGoodsEntity entity:goods){
                                    GoodsDao.getInstance(context).updateStock(entity.getGoodsId(),entity.getAmount());
                                }
                            }
                        }).start();


                        if (payType != 3) {
                            OrderEntity orderEntity = new Gson().fromJson(t, OrderEntity.class);
                            binding.pbPay.setVisibility(View.GONE);
                            binding.tvResult.setText("支付成功");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 500);
                            //发消息更新商品列表
                            EventBus.getDefault().post("refresh_goods_list");
                            //清空开单列表
                            Map<String, String> msg = new HashMap<>();
                            msg.put("name", "clear_order");
                            EventBus.getDefault().post(msg);

                            //打印小票
                            UserEntity user = SPUtils.get().getUser();
                            try {
                                USBUtils.getInstance(context).printOrder(
                                        user.getShopDomain().getName(),
                                        orderEntity.getOrderNo(),
                                        memberEntity,
                                        orderEntity.getCreateTime(),
                                        user.getName(),
                                        payType,
                                        totalMoney,
                                        payMoney,
                                        discountMoney,
                                        zeroMoney,
                                        integralMoney,
                                        goods,
                                        remark
                                );
                            } catch (Exception e) {
                                ToastUtils.showShortToast(context, "小票打印失败");
                            }
                        } else {
                            try {
                                JSONObject object = new JSONObject(t);
                                JSONObject obj = object.getJSONObject("res");
                                final int orderId = object.getInt("orderId");
                                final String orderNum = object.getString("orderNum");
                                final String createTime = object.getString("createTime");
                                if("支付成功".equals(obj.getString("res"))){
                                    // get pay way
                                    String payWay = obj.getString("payWay");
                                    int payId = getPayIdByPayWay(payWay);
                                    //支付成功
                                    HttpUtils.getInstance().updateOrder(orderId,payId)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                                @Override
                                                public void onSuccess(String t) {
                                                    binding.pbPay.setVisibility(View.GONE);
                                                    binding.tvResult.setText("支付成功");
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dialog.dismiss();
                                                        }
                                                    }, 500);
                                                    //发消息更新商品列表
                                                    EventBus.getDefault().post("refresh_goods_list");
                                                    //清空开单列表
                                                    Map<String, String> msg = new HashMap<>();
                                                    msg.put("name", "clear_order");
                                                    EventBus.getDefault().post(msg);

                                                    //打印小票
                                                    UserEntity user = SPUtils.get().getUser();
                                                    try {
                                                        USBUtils.getInstance(context).printOrder(
                                                                user.getShopDomain().getName(),
                                                                orderNum,
                                                                memberEntity,
                                                                createTime,
                                                                user.getName(),
                                                                payType,
                                                                totalMoney,
                                                                payMoney,
                                                                discountMoney,
                                                                zeroMoney,
                                                                integralMoney,
                                                                goods,
                                                                remark
                                                        );
                                                    } catch (Exception e) {
                                                        ToastUtils.showShortToast(context, "小票打印失败");
                                                    }
                                                }

                                                @Override
                                                public void onFail(ExceptionReason msg) {

                                                }
                                            });
                                }else if("支付中".equals(obj.getString("res"))){
                                    //支付中。。。开启定时线程，检查订单的状态
                                    /**
                                     * 每隔5秒 检查一次服务器的状态，是否是支付成功
                                     * 如果检查6次以上还是没有成功，就撤单
                                     */
                                    new MyCheckTimer(30*1000,5*1000,orderNum,orderId,
                                            createTime,payType,payMoney,integralMoney,goods)
                                            .start();

                                }else{
                                    binding.pbPay.setVisibility(View.GONE);
                                    binding.tvResult.setText("支付失败，请重试");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                binding.pbPay.setVisibility(View.GONE);
                                binding.tvResult.setText("支付失败，请重试");
                            }
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        binding.pbPay.setVisibility(View.GONE);
                        binding.tvResult.setText("支付失败，请重试");
                    }
                });

    }

    private int getPayIdByPayWay(String payWay) {
        switch (payWay){
            case "wx":return 3;
            case "alipay":return 4;
            case "other":return 5;
            default:
                return 5;
        }
    }

    public class MyCheckTimer extends CountDownTimer{

        private String orderNum;
        private int orderId;
        private int countDown = 0;
        private String createTime;
        private int payType;
        private int payMoney;
        private int integralMoney;
        private List<OpenOrderGoodsEntity> goods;
        private boolean isPaySuccess = false;
        MyCheckTimer(long millisInFuture, long countDownInterval,String orderNum,int orderId,String createTime,
                     int payType,int payMoney,int integralMoney,List<OpenOrderGoodsEntity> goods) {
            super(millisInFuture, countDownInterval);
            this.orderNum = orderNum;
            this.createTime = createTime;
            this.payType = payType;
            this.payMoney = payMoney;
            this.integralMoney = integralMoney;
            this.goods = goods;
            this.orderId = orderId;
            binding.tvResult.setText("等待支付结果返回，请稍后...");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            HttpUtils.getInstance().queryResult(orderNum,orderId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,false) {
                        @Override
                        public void onSuccess(String t) {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(t);
                                if(!object.has("res")) return;

                                JSONObject obj = object.getJSONObject("res");
                                if("支付成功".equals(obj.getString("res"))){
                                    //将定时器取消
                                    cancel();
                                    String payWay = obj.getString("payWay");
                                    binding.pbPay.setVisibility(View.GONE);
                                    binding.tvResult.setText("支付成功");
                                    isPaySuccess = true;
                                    //更新订单的状态
                                    //支付成功
                                    HttpUtils.getInstance().updateOrder(orderId,getPayIdByPayWay(payWay))
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                                @Override
                                                public void onSuccess(String t) {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            dialog.dismiss();
                                                        }
                                                    }, 500);
                                                    //发消息更新商品列表
                                                    EventBus.getDefault().post("refresh_goods_list");
                                                    //清空开单列表
                                                    Map<String, String> msg = new HashMap<>();
                                                    msg.put("name", "clear_order");
                                                    EventBus.getDefault().post(msg);

                                                    //打印小票
                                                    UserEntity user = SPUtils.get().getUser();
                                                    try {
                                                        USBUtils.getInstance(context).printOrder(
                                                                user.getShopDomain().getName(),
                                                                orderNum,
                                                                memberEntity,
                                                                createTime,
                                                                user.getName(),
                                                                payType,
                                                                Double.valueOf(totalMoney).intValue(),
                                                                Double.valueOf(payMoney).intValue(),
                                                                Double.valueOf(discountMoney).intValue(),
                                                                Double.valueOf(zeroMoney).intValue(),
                                                                integralMoney,
                                                                goods,
                                                                remark
                                                        );
                                                    } catch (Exception e) {
                                                        ToastUtils.showShortToast(context, "小票打印失败");
                                                    }
                                                }

                                                @Override
                                                public void onFail(ExceptionReason msg) {

                                                }
                                            });
                                }else{
                                    countDown ++;
                                    isPaySuccess = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                countDown ++;
                                isPaySuccess = false;
                            }

                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }

        @Override
        public void onFinish() {
            if(countDown>=6){
                if(!isPaySuccess){
                    //撤销当前的订单
                    HttpUtils.getInstance().backOrder(orderNum)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<String>(baseDialog,false) {
                                @Override
                                public void onSuccess(String t) {
                                    try {
                                        binding.pbPay.setVisibility(View.GONE);
                                        JSONObject object = new JSONObject(t);
                                        String res = object.getString("res");
                                        if("撤单成功".equals(res)){
                                            binding.tvResult.setText("支付失败，撤单成功，请重新开单");
                                        }else{
                                            binding.tvResult.setText("支付失败，撤单成功，请手动处理");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFail(ExceptionReason msg) {

                                }
                            });
                }
            }
        }
    }
}
