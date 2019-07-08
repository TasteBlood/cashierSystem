package com.cloudcreativity.cashiersystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;

import com.cloudcreativity.cashiersystem.utils.ToastUtils;
import com.cloudcreativity.cashiersystem.utils.USBUtils;

public class USBConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.android.example.USB_PERMISSION".equals(intent.getAction())){
            //连接成功
            UsbDevice device = intent.getParcelableExtra("device");
            if(intent.getBooleanExtra("permission",false)){
                USBUtils.getInstance(context).setDevice(device);
                USBUtils.getInstance(context).connect(context);
            }else{
                ToastUtils.showShortToast(context,"USB打印机授权失败");
            }
        }
    }
}
