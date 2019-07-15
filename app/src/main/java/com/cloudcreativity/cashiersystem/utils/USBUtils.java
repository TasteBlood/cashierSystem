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

import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;
import com.cloudcreativity.cashiersystem.receiver.USBConnectReceiver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                                    new Intent("com.android.example.USB_PERMISSION"), 0);
                            IntentFilter filter = new IntentFilter("com.android.example.USB_PERMISSION");
                            context.registerReceiver(usbReceiver, filter);
                            usbManager.requestPermission(device, pendingIntent);
                            this.device = usbDevice;
                            return;
                        } else {
                            this.device = usbDevice;
                            connect(context);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void connect(Context context) {
        if (device != null) {
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
                ToastUtils.showShortToast(context, "没有打印机接口");
                return;
            }
            UsbDeviceConnection connection = usbManager.openDevice(device);
            if (connection != null && connection.claimInterface(intf, true)) {
                usbDeviceConnection = connection;
                ToastUtils.showShortToast(context, "连接USB打印机成功");
            } else {
                //Log.i(TAG, "打开失败！ ");
                usbDeviceConnection = null;
            }
        } else {
            ToastUtils.showShortToast(context, "连接失败");
        }
    }

    public void disconnect() {
        if (usbDeviceConnection != null) {
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
                result = false;
//                Log.i(TAG, "发送失败！ " + len);
            } else {
                result = true;
//                Log.i(TAG, "发送" + len + "字节数据");
            }
        }
        return result;
    }

    public boolean openCashBox() {
        byte[] push_cash = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};
        return sendData(push_cash);
    }

    public void printTestPage() throws UnsupportedEncodingException {
        initPos();
        bold(true);
        comeChinese();
        printLocation(1);
        printText("蔬哥果妹测试店");

        printLocation(0);
        printTextNewLine("-----------------------------------------------");
        bold(false);
        printTextNewLine("订单号：620101100119071510006");
        printTextNewLine("会员ID：无会员");
        printTextNewLine("订单状态：已结算");
        printTextNewLine("订单日期：2019-07-15 12:00:00");
        printTextNewLine("收银员：徐静姝");

        bold(true);
        printTextNewLine("订单总价：200.25");
        printTextNewLine("商品折扣：-20.00");
        printTextNewLine("会员折扣：-20.00");
        printTextNewLine("积分抵扣：-10.00");
        printTextNewLine("抹零：-0.25");
        printTextNewLine("实际付款：150.00");
        printTextNewLine("付款方式：现金");
        bold(false);
        printLine(2);


        printText("商品");
        printLocation(20,1);
        printText("单价");
        printLocation(105,1);
        printText("数量");
        printWordSpace(2);
        printText("小计");
        printWordSpace(3);
        printTextNewLine("-----------------------------------------------");

        List<OpenOrderGoodsEntity> goods = new ArrayList<>();
        OpenOrderGoodsEntity entity = new OpenOrderGoodsEntity();
        entity.setGoodsName("西红柿");
        entity.setPrice(10025);
        entity.setAmount(1);
        entity.calculateMoney();
        goods.add(entity);
        entity = new OpenOrderGoodsEntity();
        entity.setGoodsName("高原夏菜");
        entity.setPrice(2000);
        entity.setAmount(4);
        entity.calculateMoney();
        goods.add(entity);
        entity = new OpenOrderGoodsEntity();
        entity.setGoodsName("阿尔山矿泉水");
        entity.setPrice(200);
        entity.setAmount(10);
        entity.calculateMoney();
        goods.add(entity);
        for(OpenOrderGoodsEntity en:goods){
            printTextNewLine(en.getGoodsName());
            printLocation(20, 1);
            printText(String.valueOf(StrUtils.get2BitDecimal(en.getPrice()/100f)));
            printLocation(105, 1);
            printWordSpace(2);
            printText(String.valueOf(en.getAmount()));
            printWordSpace(3);
            printText(String.valueOf(StrUtils.get2BitDecimal(en.getMoney()/100f)));
        }

        printTextNewLine("-----------------------------------------------");
        printLocation(0);
        printTextNewLine("备注：测试胡数据啊");
        printLine(2);

        //切纸
        feedAndCut();




    }

    public void printOrder(String shopName, String time, int totalMoney, int payMoney, int discountMoney,
                           int zeroMoney, int integralMoney, List<OpenOrderGoodsEntity> goods) {

    }

    /**
     * 打印二维码
     *
     * @param data 二维码数据
     * @throws UnsupportedEncodingException
     */
    private void qrCode(String data) throws UnsupportedEncodingException {
        int moduleSize = 8;
        int length = data.getBytes("gbk").length;

        sendData(new byte[]{0x1d});
        sendData("(k".getBytes());
        //打印二维码矩阵
        sendData(new byte[]{(byte) (length + 3)}); // pl
        sendData(new byte[]{0, 49, 80, 48});
        sendData(data.getBytes("gbk"));

        sendData(new byte[]{0x1d});
        sendData("(k".getBytes());
        sendData(new byte[]{3, 0, 49, 69, 48});

        sendData(new byte[]{0x1d});
        sendData("(k".getBytes());

        sendData(new byte[]{3, 0, 49, 67});
        sendData(new byte[]{(byte) moduleSize});

        sendData(new byte[]{0x1d});
        sendData("(k".getBytes());

        sendData(new byte[]{3, 0, 49, 81, 48});
    }

    /**
     * 进纸并全部切割
     */
    private void feedAndCut() {
        sendData(new byte[]{0x1d, 86, 65, 50});
    }

    /**
     * 打印换行
     * length 需要打印的空行数
     */
    private void printLine(int length) {
        for (int i = 0; i < length; i++) {
            sendData("\n".getBytes());
        }
    }

    /**
     * 打印换行(只换一行)
     */
    private void printLine() {
        sendData("\n".getBytes());
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     */
    private void printTabSpace(int length) {
        for (int i = 0; i < length; i++) {
            sendData("\t".getBytes());
        }
    }

    /**
     * 打印空白（一个汉字的位置）
     *
     * @param length 需要打印空白的长度,
     */
    private void printWordSpace(int length) {
        for (int i = 0; i < length; i++) {
            sendData(" ".getBytes());
        }
    }

    /**
     * 打印位置调整
     *
     * @param position 打印位置 0：居左(默认) 1：居中 2：居右
     */
    private void printLocation(int position) {
        sendData(new byte[]{0x1B, 0x61, (byte) position});
    }

    /**
     * 绝对打印位置
     */
    private void printLocation(int light, int weight) {
        sendData(new byte[]{0x1B, 0x24, (byte) light, (byte) weight});
    }

    /**
     * 打印文字
     *
     * @param text
     */
    private void printText(String text) throws UnsupportedEncodingException {
        sendData(text.getBytes("gbk"));
    }

    /**
     * 新起一行，打印文字
     *
     * @param text
     */
    private void printTextNewLine(String text) throws UnsupportedEncodingException {
        //换行
        sendData("\n".getBytes());
        sendData(text.getBytes("gbk"));
    }

    /**
     * 初始化打印机
     */
    private void initPos() {
        //init
        sendData(new byte[]{0x1B, 0x40});
    }

    /**
     * 进入汉字打印模式
     */
    private void comeChinese(){

        //进入汉字打印模式
        sendData(new byte[]{0x1C,0x26});
    }

    /**
     * 加粗
     *
     * @param flag false为不加粗
     */
    private void bold(boolean flag) {
        if (flag) {
            //常规粗细
            sendData(new byte[]{0x1B, 69, 0xf});
        } else {
            //加粗
            sendData(new byte[]{0x1B, 69, 0});
        }
    }


}
