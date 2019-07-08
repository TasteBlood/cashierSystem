package com.cloudcreativity.cashiersystem.model;

import android.content.Intent;
import android.databinding.ObservableField;
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

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangeMobileModel extends BaseModel<FragmentActivity, FragmentChangeMobileBinding> {

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();
    public ObservableField<String> code = new ObservableField<>();
    public UserEntity user;
    private BaseDialogImpl baseDialog;

    public ChangeMobileModel(FragmentActivity context, FragmentChangeMobileBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        user = SPUtils.get().getUser();
    }

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
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
}
