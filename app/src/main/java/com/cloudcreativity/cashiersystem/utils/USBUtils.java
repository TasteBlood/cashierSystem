package com.cloudcreativity.cashiersystem.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.cloudcreativity.cashiersystem.receiver.USBConnectReceiver;

import java.util.HashMap;
import java.util.Set;

public class USBUtils {
    private static USBUtils usbUtils;
    private UsbManager usbManager;
    private USBConnectReceiver usbReceiver;
    private UsbDevice device;
    private UsbEndpoint usbEndpoint;
    private UsbDeviceConnection usbDeviceConnection;

    public UsbDevice getDevice() {
        return device;
    }

    public void setDevice(UsbDevice device) {
        this.device = device;
    }

    private USBUtils(Context context) {
        //初始化USB连接工具
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        usbReceiver = new USBConnectReceiver();
    }

    public synchronized static USBUtils getInstance(Context context) {
        return usbUtils == null ? usbUtils = new USBUtils(context) : usbUtils;
    }


    //获取USB设备
    public HashMap<String, UsbDevice> getDevices() {
        if (usbManager == null)
            return null;
        return usbManager.getDeviceList();
    }

    public void startConnect(Context context) {
        if (usbManager == null)
            return;
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        if (deviceList == null || deviceList.isEmpty()) {
            ToastUtils.showShortToast(context, "未连接任何USB设备");
            return;
        }
        Set<String> strings = deviceList.keySet();
        for (String key : strings) {
            UsbDevice usbDevice = deviceList.get(key);
            if (usbDevice != null && (AppConfig.PRINTER_VENDOR_ID == usbDevice.getVendorId())) {
                //是同一个厂商的设备，直接进行连接和使用
                for (int i = 0; i < usbDevice.getInterfaceCount(); i++) {
                    UsbInterface anInterface = usbDevice.getInterface(i);
                    if (anInterface.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
                        //那就是同一个厂家的打印机设备
                        if (!usbManager.hasPermission(device)) {
                            //需要授权，才能使用
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                                    new Intent("com.android.example.USB_PERMISSION"),0);
                            IntentFilter filter = new IntentFilter("com.android.example.USB_PERMISSION");
                            context.registerReceiver(usbReceiver,filter);
                            usbManager.requestPermission(device,pendingIntent);
                            this.device = usbDevice;
                            return;
                        }else{
                            this.device = usbDevice;
                            connect(context);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void connect(Context context){
        if(device!=null){
            UsbInterface intf = null;
            UsbEndpoint ep = null;
            int InterfaceCount = device.getInterfaceCount();
            int j;
            for (j = 0; j < InterfaceCount; j++) {
                int i;
                intf = device.getInterface(j);
                if (intf.getInterfaceClass() == 7) {
                    int UsbEndpointCount = intf.getEndpointCount();
                    for (i = 0; i < UsbEndpointCount; i++) {
                        ep = intf.getEndpoint(i);
                        if (ep.getDirection() == 0 && ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            usbEndpoint = ep;
                            break;
                        }
                    }
                    if (i != UsbEndpointCount) {
                        break;
                    }
                }
            }
            if (j == InterfaceCount) {
                ToastUtils.showShortToast(context,"没有打印机接口");
                return;
            }
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection != null && connection.claimInterface(intf, true)) {
                usbDeviceConnection = connection;
                ToastUtils.showShortToast(context,"连接USB打印机成功");
            } else {
                //Log.i(TAG, "打开失败！ ");
                usbDeviceConnection = null;
            }
        }else{
            ToastUtils.showShortToast(context,"连接失败");
        }
    }

    public void disconnect(){
        if(usbDeviceConnection!=null){
            usbDeviceConnection.close();
            usbDeviceConnection = null;
            device = null;
        }
    }

    public boolean sendData(byte[] data) {
        boolean result;
        synchronized (this) {
            int len = -1;
            if (usbDeviceConnection != null) {
                len = usbDeviceConnection.bulkTransfer(usbEndpoint, data, data.length, 10000);
            }

            if (len < 0) {
                result  = false;
//                Log.i(TAG, "发送失败！ " + len);
            } else {
                result = true;
//                Log.i(TAG, "发送" + len + "字节数据");
            }
        }
        return result;
    }

    public boolean openCashBox(){
        byte[] push_cash = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};
        return sendData(push_cash);
    }
}
