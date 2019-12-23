package com.cloudcreativity.cashiersystem.model;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentChangePwdBinding;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.cloudcreativity.cashiersystem.receiver.MyBusinessReceiver;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.cloudcreativity.cashiersystem.utils.VerifyCodeDialogUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangePwdModel extends BaseModel<FragmentActivity, FragmentChangePwdBinding> {

    public ObservableField<String> pwd1 = new ObservableField<>();
    public ObservableField<String> pwd2 = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();
    public UserEntity user;
    private BaseDialogImpl baseDialog;
    private static final long TOTAL_TIME = 2 * 60 * 1000;
    private CountDownTimer timer;

    public ChangePwdModel(FragmentActivity context, FragmentChangePwdBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        user = SPUtils.get().getUser();
    }

    public void onBack() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
    }

    public void onSendClick() {
        new VerifyCodeDialogUtils().show(context, new VerifyCodeDialogUtils.OnOkListener() {
            @Override
            public void onOk(String code) {
                //
                HttpUtils.getInstance().sendSms(user.getMobile())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog, false) {
                            @Override
                            public void onSuccess(String t) {
                                ToastUtils.showShortToast(context, "验证码发送成功");
                                startTimer(TOTAL_TIME);
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        }, baseDialog);
    }

    public void onSaveClick() {
        if (TextUtils.isEmpty(pwd1.get())) {
            ToastUtils.showShortToast(context, "新密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd2.get())) {
            ToastUtils.showShortToast(context, "确认密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(code.get())) {
            ToastUtils.showShortToast(context, "验证码");
            return;
        }

        if (!pwd1.get().equals(pwd2.get())) {
            ToastUtils.showShortToast(context, "两次密码不一致");
        }

        HttpUtils.getInstance().checkSms(user.getMobile(), code.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, true) {
                    @Override
                    public void onSuccess(String t) {
                        HttpUtils.getInstance().changePwd(user.getId(), pwd1.get(), pwd2.get(), code.get())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog, true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        ToastUtils.showShortToast(context, "修改成功,需要重新登录");
                                        Intent intent = new Intent();
                                        intent.setAction(MyBusinessReceiver.ACTION_LOGOUT);
                                        context.sendBroadcast(intent);
                                    }

                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void stopTimer() {
        if (timer != null)
            timer.cancel();
        binding.btnSend.setText("发送验证码");
        binding.btnSend.setEnabled(true);
    }

    public void startTimer(long smsTime) {
        timer = null;
        timer = new CountDownTimer(smsTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.btnSend.setText(millisUntilFinished / 1000 + "秒后");
                SPUtils.get().setSMSTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                binding.btnSend.setEnabled(true);
                binding.btnSend.setText("发送验证码");
            }
        };
        timer.start();
        binding.btnSend.setEnabled(false);
    }
}
