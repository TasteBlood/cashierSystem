package com.cloudcreativity.cashiersystem.model;

import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentLogBinding;
import com.cloudcreativity.cashiersystem.utils.AppConfig;

import org.greenrobot.eventbus.EventBus;

public class LogModel extends BaseModel<FragmentActivity, FragmentLogBinding> {
    private BaseDialogImpl baseDialog;

    public ObservableField<String> startTime = new ObservableField<>();
    public ObservableField<String> exitTime = new ObservableField<>();

    public LogModel(FragmentActivity context, FragmentLogBinding binding, BaseDialogImpl baseDialog) {
        super(context, binding);
        this.baseDialog = baseDialog;
        startTime.set(BaseApp.LOGIN_TIME);
        exitTime.set(BaseApp.LOGOUT_TIME);
        binding.rgType.check(R.id.rb_log_cashier);
    }

    public void onBack() {
        EventBus.getDefault().post(AppConfig.FRAGMENT_NAMES.FRAGMENT_PERSONAL_DEFAULT);
        BaseApp.LOGOUT_TIME = "";
        BaseApp.LOGIN_TIME = "";
    }

    public void onCheckChange(RadioGroup rg, int id) {
        switch (id) {
            case R.id.rb_log_cashier:
                binding.layoutCash.setVisibility(View.VISIBLE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.GONE);
                break;
            case R.id.rb_log_create:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.VISIBLE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.GONE);
                break;
            case R.id.rb_log_edit:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.VISIBLE);
                binding.layoutRecharge.setVisibility(View.GONE);
                break;
            case R.id.rb_log_recharge:
                binding.layoutCash.setVisibility(View.GONE);
                binding.layoutCreate.setVisibility(View.GONE);
                binding.layoutEdit.setVisibility(View.GONE);
                binding.layoutRecharge.setVisibility(View.VISIBLE);
                break;
        }
    }
}
