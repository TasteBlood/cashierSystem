package com.cloudcreativity.cashiersystem.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * 扫码枪输入辅助服务
 */
public class ScanService extends AccessibilityService {
    private static OnKeyEvent onKeyEvent;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if(onKeyEvent!=null){
            //这里通过回调的方式将事件传出去统一处理
            //返回true事件就会拦截不会继续传递
            return onKeyEvent.onKeyEvent(event);
        }
        return super.onKeyEvent(event);
    }
    /**
     * 设置监听
     * @param onKeyEvent
     */
    public static void setOnKeyEvent(OnKeyEvent onKeyEvent){
        ScanService.onKeyEvent=onKeyEvent;
    }
    public interface OnKeyEvent{
        boolean onKeyEvent(KeyEvent event);
    }
}
