package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.databinding.LayoutDialogRechargeBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RechargeDialogUtils {
    private Dialog dialog;
    private ViewStub stepOne;
    private ViewStub stepTwo;
    private ViewStub stepThree;
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
                startRecharge(memberEntity.getId(),totalMoney*100,AppConfig.PAY_WAY.PAY_MONEY,memberEntity.getMobile());
            }
        });

        content.findViewById(R.id.btn_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepTwo.setVisibility(View.GONE);
                initStepThree(content);
                startRecharge(memberEntity.getId(),totalMoney*100,AppConfig.PAY_WAY.PAY_MONEY,memberEntity.getMobile());
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
    }

    private void startRecharge(long mid, int money, int payType,String mobile) {
        HttpUtils.getInstance().recharge(mid, money, payType,mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        //充值成功
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

                    @Override
                    public void onFail(ExceptionReason msg) {
                        //创建成功，展示创建成功的对话框，并且更新数据
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
                });
    }

}
