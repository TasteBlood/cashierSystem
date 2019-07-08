package com.cloudcreativity.cashiersystem.model;

import android.content.Intent;
import android.databinding.ObservableField;
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

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangePwdModel extends BaseModel<FragmentActivity,FragmentChangePwdBinding> {

    public ObservableField<String> pwd1 = new ObservableField<>();
    public ObservableField<String> pwd2 = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();
    public UserEntity user;
    private BaseDialogImpl baseDialog;

    public ChangePwdModel(FragmentActivity context, FragmentChangePwdBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        user = SPUtils.get().getUser();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
    }

    public void onSaveClick(){
        if(TextUtils.isEmpty(pwd1.get())){
            ToastUtils.showShortToast(context,"新密码不能为空");
            return;
        }

        if(TextUtils.isEmpty(pwd2.get())){
            ToastUtils.showShortToast(context,"确认密码不能为空");
            return;
        }

        if(TextUtils.isEmpty(code.get())){
            ToastUtils.showShortToast(context,"验证码");
            return;
        }

        if(!pwd1.get().equals(pwd2.get())){
            ToastUtils.showShortToast(context,"两次密码不一致");
        }

        HttpUtils.getInstance().changePwd(user.getId(),pwd1.get(),pwd2.get(),code.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,"修改成功,需要重新登录");
                        Intent intent = new Intent();
                        intent.setAction(MyBusinessReceiver.ACTION_LOGOUT);
                        context.sendBroadcast(intent);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }
}
