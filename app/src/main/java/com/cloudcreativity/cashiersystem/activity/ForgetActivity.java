package com.cloudcreativity.cashiersystem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        et_mobile = findViewById(R.id.et_mobile);
        et_code = findViewById(R.id.et_code);
        et_pwd = findViewById(R.id.et_pwd);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerifyCodeDialogUtils().show(ForgetActivity.this, "2345", new VerifyCodeDialogUtils.OnOkListener() {
                    @Override
                    public void onOk(String code) {

                    }
                });
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    private void save(){
        String mobile = et_mobile.getText().toString();
        String code = et_code.getText().toString();
        String password = et_pwd.getText().toString();

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
        HttpUtils.getInstance().forgetPwd(mobile,code,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(this,true) {
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
}
