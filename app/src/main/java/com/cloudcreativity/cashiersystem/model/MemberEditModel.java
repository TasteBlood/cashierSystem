package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberEditBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.StrUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberEditModel extends BaseModel<FragmentActivity, FragmentMemberEditBinding> {

    public ObservableField<MemberEntity> member = new ObservableField<>();

    private BaseDialogImpl baseDialog;

    public MemberEditModel(FragmentActivity context, FragmentMemberEditBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        loadData();
    }

    private void loadData() {
        HttpUtils.getInstance().queryMember(BaseApp.CURRENT_MID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog, false) {
                    @Override
                    public void onSuccess(String t) {
                        MemberEntity entity = new Gson().fromJson(t, MemberEntity.class);
                        member.set(entity);
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });
    }

    public void onBack() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_LIST);
        BaseApp.CURRENT_MID = 0;
        member.set(null);
    }

    public void onSave() {
        assert member.get() != null;
        if (TextUtils.isEmpty(Objects.requireNonNull(member.get()).getMobile()) ||
                !StrUtils.isPhone(Objects.requireNonNull(member.get()).getMobile())) {
            ToastUtils.showShortToast(context,"手机号不正确");
            return;
        }
        HttpUtils.getInstance().editMember(member.get().getId(),member.get().getMobile())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,true) {
                    @Override
                    public void onSuccess(String t) {
                        ToastUtils.showShortToast(context,"修改成功");
                        EventBus.getDefault().post("refresh_member_list");
                    }

                    @Override
                    public void onFail(ExceptionReason msg) {

                    }
                });

    }
}
