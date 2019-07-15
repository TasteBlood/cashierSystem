package com.cloudcreativity.cashiersystem.utils;

import android.os.Handler;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.KeyEvent;

public class ScanGunHelper {
    private static volatile ScanGunHelper mInstance;
    private final static long MESSAGE_DELAY = 500;             //延迟500ms，判断扫码是否完成。
    private StringBuffer mStringBufferResult;                  //扫码内容
    private boolean mCaps;                                     //大小写区分
    private Handler mHandler;
    private Runnable mScanningFishedRunnable;
    private ScanGunHelper.OnScanSuccessListener mOnScanSuccessListener;

    private ScanGunHelper() {
        mStringBufferResult = new StringBuffer();
        mHandler = new Handler();
        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }

    public static ScanGunHelper getInstance() {
        if (null == mInstance) {
            synchronized (ScanGunHelper.class) {
                if (null == mInstance) {
                    mInstance = new ScanGunHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 返回扫码成功后的结果
     */
    private void performScanSuccess() {
        try {
            String barcode = mStringBufferResult.toString();
            if (mOnScanSuccessListener != null && !TextUtils.isEmpty(barcode)) {
                mOnScanSuccessListener.onSuccess(barcode);
            }
            mStringBufferResult.setLength(0);
            if (mHandler != null) {
                mHandler.removeCallbacks(mScanningFishedRunnable);
            }
            mOnScanSuccessListener = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 扫码枪事件解析
     */
    public void analysisKeyEvent(KeyEvent event, ScanGunHelper.OnScanSuccessListener listener) {
        if (!isScanGunEvent(event)) {
            return;
        }
        //Virtual是我所使用机器的内置软键盘的名字
        //在这判断是因为项目中避免和软键盘冲突（扫码枪和软键盘都属于按键事件）
        if (!event.getDevice().getName().equals("Virtual")) {
            int keyCode = event.getKeyCode();
            //字母大小写判断
            checkLetterStatus(event);
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                mOnScanSuccessListener = listener;
                char aChar = getInputCode(event);
                if (aChar != 0) {
                    mStringBufferResult.append(aChar);
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    mHandler.removeCallbacks(mScanningFishedRunnable);
                    mHandler.post(mScanningFishedRunnable);
                } else {
                    mHandler.removeCallbacks(mScanningFishedRunnable);
                    mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
                }
            }
        }
    }

    //检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            //按着shift键，表示大写
            //松开shift键，表示小写
            mCaps = event.getAction() == KeyEvent.ACTION_DOWN;
        }
    }

    //获取扫描内容
    private char getInputCode(KeyEvent event) {
        int keyCode = event.getKeyCode();
        char aChar;
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }
        return aChar;
    }


    /**
     * 扫码成功回调接口
     */
    public interface OnScanSuccessListener {
        void onSuccess(String barcode);
    }

    /**
     * 输入设备是否存在
     */
    private boolean isInputDeviceExist(String deviceName) {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int id : deviceIds) {
            if (InputDevice.getDevice(id).getName().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否为扫码枪事件(部分机型KeyEvent获取的名字错误)
     */
    private boolean isScanGunEvent(KeyEvent event) {
        return isInputDeviceExist(event.getDevice().getName());
    }
}
