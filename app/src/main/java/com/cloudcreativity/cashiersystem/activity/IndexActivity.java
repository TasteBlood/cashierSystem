package com.cloudcreativity.cashiersystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IndexActivity extends BaseActivity {
    private EditText et_mobile;
    private EditText et_pass;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SPUtils.get().isLogin()){
            startActivity(new Intent(IndexActivity.this,MainActivity.class));
            finish();
            return;
        }else{
            setContentView(R.layout.activity_index);
        }
        et_mobile = findViewById(R.id.et_mobile);
        et_pass = findViewById(R.id.et_pass);


        findViewById(R.id.tv_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this,ForgetActivity.class));
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先跳转到主页
                login();
            }
        });
    }

    private void login(){
        String mobile = et_mobile.getText().toString();
        String pass = et_pass.getText().toString();

        if(!StrUtils.isPhone(mobile)){
            ToastUtils.showShortToast(this,"手机号不正确");
            return;
        }

        if(TextUtils.isEmpty(pass)){
            ToastUtils.showShortToast(this,"密码不能为空");
            return;
        }

        HttpUtils.getInstance().login(mobile,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(this,true) {
                    @Override
                    public void onSuccess(String t) {
                        UserEntity userEntity = new Gson().fromJson(t, UserEntity.class);
                        if(userEntity!=null){
                            SPUtils.get().putString(SPUtils.Config.USER,new Gson().toJson(userEntity));
                            SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
                            SPUtils.get().putString(SPUtils.Config.UID,userEntity.getId());
                            SPUtils.get().putBoolean(SPUtils.Config.IS_LOGIN,true);
                            SPUtils.get().putString(SPUtils.Config.SHOP_ID,userEntity.getShopId());
                            startActivity(new Intent(IndexActivity.this,MainActivity.class));
                            finish();
                        }else{
                            ToastUtils.showShortToast(IndexActivity.this,"登录失败");
                        }
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
