package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentMemberDetailBinding;
import com.cloudcreativity.cashiersystem.entity.MemberEntity;
import com.cloudcreativity.cashiersystem.utils.AppConfig;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MemberDetailModel extends BaseModel<FragmentActivity, FragmentMemberDetailBinding> {

    public ObservableField<MemberEntity> member = new ObservableField<>();

    private BaseDialogImpl baseDialog;
    public MemberDetailModel(FragmentActivity context, FragmentMemberDetailBinding binding,BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        loadData();
    }

    private void loadData(){
        HttpUtils.getInstance().queryMember(BaseApp.CURRENT_MID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
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

    public void onBack(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_LIST);
        BaseApp.CURRENT_MID = 0;
        member.set(null);
    }

    public void onBalanceClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_BALANCE);
    }

    public void onScoreClick(){
        //暂停

        //EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_SCORE);
    }

    public void onPayClick(){
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_MEMBER_PAY);
    }
}
