package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
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
import com.google.gson.Gson;

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
    private int totalMoney;

    //订单折扣数
    private int discountMoney;

    //订单商品总数
    private float amount;

    //订单商品信息
    private List<OpenOrderGoodsEntity> entities;

    private int finalMoney;

    //去零的剩值
    private int zeroMoney;
    //去零的差值
    private int zeroValue;

    private TextView tv_money;
    private TextView tv_zero;
    private LayoutOpenOrderFinalBinding binding;
    private MemberEntity memberEntity;
    private Activity context;
    private String remark;
    /**
     *
     * @param totalMoney 订单总额
     * @param discountMoney 折扣总计
     * @param amount 商品总数
     * @param memberEntity 会员信息
     * @param entities 商品信息
     */
    public OpenOrderFinalDialog(int totalMoney, int discountMoney, float amount,
                                MemberEntity memberEntity,String remark,List<OpenOrderGoodsEntity> entities) {
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
        tv_money.setText("￥"+StrUtils.get2BitDecimal(finalMoney/100f));
        tv_zero = binding.getRoot().findViewById(R.id.tv_zero);
        if(this.memberEntity==null){
            binding.tvBalance.setText("");
            binding.btnBalance.setEnabled(false);
        }else{
            if(TextUtils.isEmpty(this.memberEntity.getBalance())){
                binding.tvBalance.setText("");
                binding.btnBalance.setEnabled(false);
            }else{
                int balance = Integer.parseInt(memberEntity.getBalance());
                if(this.finalMoney>balance){
                    binding.tvBalance.setText("(会员余额:￥"+StrUtils.get2BitDecimal(balance/100f)+")");
                    binding.btnBalance.setEnabled(false);
                }else{
                    binding.tvBalance.setText("(会员余额:￥"+StrUtils.get2BitDecimal(balance/100f)+")");
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

    public void zeroClick(){
        int money = this.finalMoney;
        if(money%100!=0){
            //说明不是是整元，需要摸0
            int zero = money%100;
            zeroMoney = money - zero;
            zeroValue = zero;
            this.tv_zero.setText("￥"+StrUtils.get2BitDecimal(zeroMoney/100f));

            // 去0判断余额够不够
            if(this.memberEntity==null){
                binding.tvBalance.setText("");
                binding.btnBalance.setEnabled(false);
            }else{
                if(TextUtils.isEmpty(this.memberEntity.getBalance())){
                    binding.tvBalance.setText("");
                    binding.btnBalance.setEnabled(false);
                }else{
                    int balance = Integer.parseInt(this.memberEntity.getBalance());
                    if(zeroMoney>balance){
                        binding.tvBalance.setText("(会员余额:￥"+StrUtils.get2BitDecimal(balance/100f)+")");
                        binding.btnBalance.setEnabled(false);
                    }else{
                        binding.tvBalance.setText("(会员余额:￥"+StrUtils.get2BitDecimal(balance/100f)+")");
                        binding.btnBalance.setEnabled(true);
                    }
                }
            }
        }else{
            //整元不需要
            tv_zero.setText("￥"+StrUtils.get2BitDecimal(this.finalMoney/100f));
        }
    }

    public void onBack() {
        if (dialog != null)
            dialog.dismiss();
    }

    public void onMobileClick(){
        binding.layoutPay.setVisibility(View.VISIBLE);
    }

    public void onMoneyClick(){
        binding.layoutPay.setVisibility(View.VISIBLE);
        USBUtils.getInstance(context).openCashBox();
        long mid = 0;
        String mobile = null;
        if(memberEntity!=null){
            mid = memberEntity.getId();
            mobile = memberEntity.getMobile();
        }
        submitOrder(totalMoney,finalMoney,zeroMoney,discountMoney,0,zeroValue,mid,mobile,
                AppConfig.PAY_WAY.PAY_MONEY,1,remark,entities);
    }

    public void onBalanceClick(){
        binding.layoutPay.setVisibility(View.VISIBLE);
        //binding.layoutPay.setVisibility(View.VISIBLE);
        long mid = 0;
        String mobile = null;
        if(memberEntity!=null){
            mid = memberEntity.getId();
            mobile = memberEntity.getMobile();
        }
        submitOrder(totalMoney,finalMoney,zeroMoney,discountMoney,0,zeroValue,mid,mobile,
                AppConfig.PAY_WAY.PAY_BALANCE,1,remark,entities);
    }


    private void submitOrder(int totalMoney,int finalMoney,int payMoney,int discountMoney,
                             int integralMoney,int zeroMoney,long memberId,String mobile,
                             int payType,int type,String remark,List<OpenOrderGoodsEntity> goods){
        binding.pbPay.setVisibility(View.VISIBLE);
        binding.tvResult.setText("支付中，请稍后...");
        StringBuilder builder = new StringBuilder();
        builder.append("totalMoney=").append(totalMoney).append("\n");
        builder.append("finalMoney=").append(finalMoney).append("\n");
        builder.append("discountMoney=").append(discountMoney).append("\n");
        builder.append("zeroMoney=").append(zeroMoney).append("\n");
        builder.append("integralMoney=").append(integralMoney).append("\n");
        builder.append("payMoney=").append(payMoney).append("\n");

        LogUtils.e("xuxiwu",builder.toString());
        //开始组装参数
        HttpUtils.getInstance().submitOrder(discountMoney,integralMoney,memberId,mobile,
                payMoney,payType,totalMoney,finalMoney,type,zeroMoney,remark,new Gson().toJson(goods))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        binding.pbPay.setVisibility(View.GONE);
                        binding.tvResult.setText("支付成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },500);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        binding.pbPay.setVisibility(View.GONE);
                        binding.tvResult.setText("支付失败，请重试");
                    }
                });

    }
}
