package com.cloudcreativity.cashiersystem.model;

import android.support.v4.app.FragmentActivity;

import com.cloudcreativity.cashiersystem.base.BaseModel;
import com.cloudcreativity.cashiersystem.databinding.FragmentSettingDefaultBinding;
import com.cloudcreativity.cashiersystem.utils.CallDialogUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.cloudcreativity.cashiersystem.utils.USBUtils;

import java.io.UnsupportedEncodingException;

public class SettingDefaultModel extends BaseModel<FragmentActivity, FragmentSettingDefaultBinding> {

    public SettingDefaultModel(FragmentActivity context, FragmentSettingDefaultBinding binding) {
        super(context, binding);
    }

    public void onConnectPrinterClick(){
        USBUtils.getInstance(context).startConnect(context);
    }

    public void onTestPrintClick(){
        try {
            USBUtils.getInstance(context).printTestPage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void onOpenCashBoxClick(){
        USBUtils.getInstance(context).openCashBox();
    }

    public void onCallOpenClick(){
        new CallDialogUtils().show(context, new CallDialogUtils.OnOkListener() {
            @Override
            public void onOk(double number) {
                ToastUtils.showShortToast(context,"这次称重是"+number+"kg");
            }
        },"测试数据",null);
    }
}
