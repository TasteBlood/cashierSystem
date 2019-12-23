package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogRechargeBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.entity.UserEntity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RechargeDialogUtils {
    private Dialog dialog;
    private ViewStub stepOne;
    private ViewStub stepTwo;
    private ViewStub stepThree;
    private TextView tv_result;
    private BaseDialogImpl baseDialog;
    private Context context;
    private MemberEntity memberEntity;
    private int totalMoney;
    public RechargeDialogUtils(MemberEntity memberEntity, Context context, BaseDialogImpl baseDialog) {
        this.memberEntity = memberEntity;
        this.baseDialog = baseDialog;
        this.context = context;
    }

    public void show(Activity context) {
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LayoutDialogRechargeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_dialog_recharge, null, false);
        binding.setUtils(this);
        dialog.setContentView(binding.getRoot());
        initStepOne(binding.getRoot());
        int width = context.getResources().getDisplayMetrics().widthPixels;
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = width / 3;
        dialog.show();
    }

    private void initStepOne(final View content) {
        stepOne = content.findViewById(R.id.stepOne);
        stepOne.inflate();

        content.findViewById(R.id.btn_cancel_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        content.findViewById(R.id.btn_ok_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_money = content.findViewById(R.id.et_money);
                String money = et_money.getText().toString();
                if (TextUtils.isEmpty(money))
                    return;
                if (Integer.parseInt(money) < 200)
                    return;
                totalMoney = Integer.parseInt(money);
                stepOne.setVisibility(View.GONE);
                SoftKeyboardUtils.hideSoftInput(et_money);
                initStepTwo(content);
            }
        });
    }

    private void initStepTwo(final View content) {
        stepTwo = content.findViewById(R.id.stepTwo);
        stepTwo.inflate();
        content.findViewById(R.id.btn_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTwo.setVisibility(View.GONE);
                initStepThree(content);
                startRechargeByMoney(memberEntity.getId(),totalMoney*100,AppConfig.PAY_WAY.PAY_MONEY,memberEntity.getMobile());
            }
        });

        content.findViewById(R.id.btn_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScanCodeDialog codeDialog = new ScanCodeDialog(context, R.style.myProgressDialogStyle);
                codeDialog.setOnOkListener(new ScanCodeDialog.OnOkListener() {
                    @Override
                    public void onOk(String code) {
                        stepTwo.setVisibility(View.GONE);
                        initStepThree(content);
                        startRechargeByMobile(memberEntity.getId(),totalMoney*100,AppConfig.PAY_WAY.PAY_MOBILE,memberEntity.getMobile(),code);
                    }
                });
                codeDialog.show();



                //startRecharge(memberEntity.getId(),totalMoney*100,AppConfig.PAY_WAY.PAY_MONEY,memberEntity.getMobile());
            }
        });

        content.findViewById(R.id.btn_cancel_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void initStepThree(final View content) {
        stepThree = content.findViewById(R.id.stepThree);
        stepThree.inflate();
        content.findViewById(R.id.btn_cancel_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_result = content.findViewById(R.id.btn_wait);
    }

    private void startRechargeByMoney(long mid, int money, int payType,String mobile) {
        UserEntity user = SPUtils.get().getUser();
        HttpUtils.getInstance().recharge(mid, money, payType,mobile,user.getId(),"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        //充值成功
                        showSuccess();
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        //创建成功，展示创建成功的对话框，并且更新数据
                        showFailed();
                    }
                });
    }

    private void showSuccess(){
        //创建成功，展示创建成功的对话框，并且更新数据
        dialog.dismiss();
        final Dialog dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        View content = View.inflate(context, R.layout.layout_dialog_recharge_success, null);
        dialog.setContentView(content);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels / 3;
        dialog.show();
        //发消息更新会员列表
        EventBus.getDefault().post("refresh_member_list");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    private void showFailed(){
        dialog.dismiss();
        final Dialog dialog = new Dialog(context, R.style.myProgressDialogStyle);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        View content = View.inflate(context, R.layout.layout_dialog_recharge_failed, null);
        dialog.setContentView(content);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        window.getAttributes().width = context.getResources().getDisplayMetrics().widthPixels / 3;
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);
    }

    private void startRechargeByMobile(long mid,int money,int payType,String mobile,String code){
        UserEntity user = SPUtils.get().getUser();
        HttpUtils.getInstance().recharge(mid, money, payType,mobile,user.getId(),code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        //下单成功，支付完成
                        LogUtils.e("xuxiwu",t);
                        try {
                            JSONObject obj = new JSONObject(t);
                            int oid = obj.getInt("id");
                            if("支付成功".equals(obj.getString("res"))){
                                //更新订单状态
                                HttpUtils.getInstance().updateRecharge(oid)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DefaultObserver<String>(baseDialog,false) {
                                            @Override
                                            public void onSuccess(String t) {
                                                showSuccess();
                                            }

                                            @Override
                                            public void onFail(ExceptionReason msg) {
                                                showFailed();
                                            }
                                        });
                            }else if("支付中".equals(obj.getString("res"))){
                                //启动线程检查支付结果
                                String orderNum = obj.getString("rechargeNo");

                                new MyCheckTimer(30*1000,5*1000,orderNum,oid).start();
                            }else{
                                // 支付失败
                                showFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        showFailed();
                    }
                });
    }

    public class MyCheckTimer extends CountDownTimer {

        private String orderNum;
        private int oid;
        private int countDown = 0;
        private boolean isPaySuccess = false;
        MyCheckTimer(long millisInFuture, long countDownInterval,String orderNum,int oid) {
            super(millisInFuture, countDownInterval);
            this.orderNum = orderNum;
            this.oid = oid;
            // binding.tvResult.setText("等待支付结果返回，请稍后...");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            HttpUtils.getInstance().queryResult(orderNum,oid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(baseDialog,false) {
                        @Override
                        public void onSuccess(String t) {
                            JSONObject object = null;
                            try {
                                object = new JSONObject(t);
                                String res = object.getString("res");
                                if("支付成功".equals(res)){
                                    //将定时器取消
                                    cancel();
                                    isPaySuccess = true;
                                    //更新订单状态
                                    HttpUtils.getInstance().updateRecharge(oid)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DefaultObserver<String>(baseDialog,false) {
                                                @Override
                                                public void onSuccess(String t) {
                                                    showSuccess();
                                                }

                                                @Override
                                                public void onFail(ExceptionReason msg) {
                                                    showFailed();
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
                                        JSONObject object = new JSONObject(t);
                                        String res = object.getString("res");
                                        if("撤单成功".equals(res)){
                                            tv_result.setText("支付失败，撤单成功，请重新开单");
                                        }else{
                                            tv_result.setText("支付失败，撤单成功，请手动处理");
                                        }

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                showFailed();
                                            }
                                        }, 1000);
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
