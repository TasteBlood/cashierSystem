package com.cloudcreativity.cashiersystem.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.cloudcreativity.cashiersystem.utils.VerifyCodeDialogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity {

    private EditText et_mobile;
    private EditText et_code;
    private EditText et_pwd;
    private Button btn_send;
    private static final int TOTAL_TIME = 2*60*1000;
    private CountDownTimer timer;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        et_mobile = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);
        et_pwd = findViewById(R.id.et_pwd);
        btn_send = findViewById(R.id.btn_send);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_mobile.getText()) || !StrUtils.isPhone(String.valueOf(et_mobile.getText()))){
                    ToastUtils.showShortToast(getBaseContext(),"手机号不正确");
                    return;
                }
                new VerifyCodeDialogUtils().show(ForgetActivity.this, new VerifyCodeDialogUtils.OnOkListener() {
                    @Override
                    public void onOk(String code) {
                        //发送验证码
                        HttpUtils.getInstance().sendSms(et_mobile.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(ForgetActivity.this,false) {
                                    @Override
                                    public void onSuccess(String t) {
                                        ToastUtils.showShortToast(getBaseContext(),t);
                                        //发送成功，开启定时器
                                        startTimer(TOTAL_TIME);
                                    }

                                    @Override
                                    public void onFail(ExceptionReason msg) {

                                    }
                                });
                    }
                },ForgetActivity.this);
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


        //判断上次的时间是否为空
        if(SPUtils.get().getSMSTime()>0){
            btn_send.setEnabled(false);
            startTimer(SPUtils.get().getSMSTime());
        }
    }

    private void save(){

        final String mobile = String.valueOf(et_mobile.getText());
        final String code = String.valueOf(et_code.getText());
        final String password = String.valueOf(et_pwd.getText());

        if(TextUtils.isEmpty(mobile) || !StrUtils.isPhone(mobile)){
            ToastUtils.showShortToast(this,"手机号不正确");
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtils.showShortToast(this,"验证码不能为空");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShortToast(this,"密码不能为空");
            return;
        }
        //先检查短信验证码是否正确
        HttpUtils.getInstance().checkSms(mobile,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(this,true) {
                    @Override
                    public void onSuccess(String t) {
                        HttpUtils.getInstance().forgetPwd(mobile,code,password)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<String>(ForgetActivity.this,true) {
                                    @Override
                                    public void onSuccess(String t) {
                                        ToastUtils.showShortToast(ForgetActivity.this,"修改成功");
                                        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void startTimer(long time){
        timer = null;
        timer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_send.setText(((millisUntilFinished)/1000)+"秒后");
                //在这里将剩余的时间保存再sp里面，防止下次打开时间无效
                SPUtils.get().setSMSTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                SPUtils.get().setSMSTime(0);
                btn_send.setEnabled(true);
                btn_send.setText("发送验证码");
            }
        };
        timer.start();
        btn_send.setEnabled(false);
    }

    private void stopTimer(){
        if(timer!=null){
            timer.cancel();
        }
        btn_send.setEnabled(true);
        btn_send.setText("发送验证码");
    }
}
