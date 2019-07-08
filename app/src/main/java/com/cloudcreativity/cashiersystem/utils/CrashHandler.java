package com.cloudcreativity.cashiersystem.utils;

import android.content.Context;

/**
 * 全局的异常处理器
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private  static CrashHandler INSTANCE;
    // 程序的Context对象
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE==null?INSTANCE = new CrashHandler():INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处
            mDefaultHandler.uncaughtException(t, e);
        } else {
            // 跳转到崩溃提示Activity
            ToastUtils.showShortToast(mContext,"系统发生异常");
            System.exit(0);// 关闭已奔溃的app进程
        }
    }

    /**
     * 自定义错误捕获
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集错误信息
        getCrashInfo(ex);

        return true;
    }

    private void getCrashInfo(Throwable ex) {
        LogUtils.e("xuxiwu",ex.getLocalizedMessage());
    }
}
