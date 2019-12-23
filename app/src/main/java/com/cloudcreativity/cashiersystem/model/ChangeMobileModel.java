package com.cloudcreativity.cashiersystem.model;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentChangeMobileBinding;
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

public class ChangeMobileModel extends BaseModel<FragmentActivity, FragmentChangeMobileBinding> {

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();
    public UserEntity user;
    private BaseDialogImpl baseDialog;
    private static final int TOTAL_TIME = 2 * 60 * 1000;
    private CountDownTimer timer;

    public ChangeMobileModel(FragmentActivity context, FragmentChangeMobileBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        user = SPUtils.get().getUser();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
    }

    public void onSendClick(){
        if(!StrUtils.isPhone(phone.get())){
            ToastUtils.showShortToast(context,"手机号不正确");
            return;
        }
        new VerifyCodeDialogUtils().show(context, new VerifyCodeDialogUtils.OnOkListener() {
            @Override
            public void onOk(String code) {
                //
                HttpUtils.getInstance().sendSms(phone.get())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(baseDialog,false) {
                            @Override
                            public void onSuccess(String t) {
                                startTimer(TOTAL_TIME);
                                ToastUtils.showShortToast(context,"验证码发送成功");
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });
            }
        },baseDialog);
    }

    public void onSaveClick(){
        if(!StrUtils.isPhone(phone.get())){
            ToastUtils.showShortToast(context,"手机号不正确");
            return;
        }

        if(TextUtils.isEmpty(pwd.get())){
            ToastUtils.showShortToast(context,"密码不能为空");
            return;
        }

        if(TextUtils.isEmpty(code.get())){
            ToastUtils.showShortToast(context,"验证码");
            return;
        }

        HttpUtils.getInstance().checkSms(phone.get(),code.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        HttpUtils.getInstance().changeMobile(phone.get(),code.get(),pwd.get(),user.getMobile())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        ToastUtils.showShortToast(context,"修改成功,需要重新登录");
                                        Intent intent = new Intent(MyBusinessReceiver.ACTION_LOGOUT);
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

    public void startTimer(long smsTime) {
        timer = null;
        timer = new CountDownTimer(smsTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.btnSend.setText(millisUntilFinished/1000+"秒后");
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

    public void stopTimer() {
        if(timer!=null)
            timer.cancel();
        binding.btnSend.setEnabled(true);
        binding.btnSend.setText("发送验证码");
    }
}
