package com.cloudcreativity.cashiersystem.activity;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.base.BaseActivity;
import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.databinding.ActivityMainBinding;
import com.cloudcreativity.cashiersystem.model.MainActivityModel;
import com.cloudcreativity.cashiersystem.receiver.MyBusinessReceiver;
import com.cloudcreativity.cashiersystem.utils.DefaultObserver;
import com.cloudcreativity.cashiersystem.utils.HttpUtils;
import com.cloudcreativity.cashiersystem.utils.LogUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private MyBusinessReceiver receiver;

    //无操作最大时长
    private static final int MAX_TIME = 60 * 60 * 1000;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MyBusinessReceiver.ACTION_LOGOUT);
        filter.addAction(MyBusinessReceiver.ACTION_EXIT_APP);

        receiver = new MyBusinessReceiver();

        registerReceiver(receiver, filter);

        //注册业务广播
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(new MainActivityModel(this, binding));

        //开启误操作状态监听
        startAD();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtils.showShortToast(this, "再按一次退出");
                firstTime = System.currentTimeMillis();
            } else {
                //退出
                HttpUtils.getInstance().logout(SPUtils.get().getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<String>(MainActivity.this, true) {
                            @Override
                            public void onSuccess(String t) {
                                //首先清空所有的用户数据
                                SPUtils spUtils = SPUtils.get();
                                spUtils.putBoolean(SPUtils.Config.IS_LOGIN, false);
                                spUtils.putString(SPUtils.Config.UID, "");
                                spUtils.putString(SPUtils.Config.TOKEN, null);
                                spUtils.putString(SPUtils.Config.USER, "{}");
                                spUtils.putString(SPUtils.Config.SHOP_ID, "");
                                showUserAuthOutDialog();
                                finish();
                                BaseApp.app.onTerminate();
                            }

                            @Override
                            public void onFail(ExceptionReason msg) {

                            }
                        });

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            handler.removeCallbacks(runnable);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            startAD();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void startAD() {
        LogUtils.e("xuxiwu", "开始计时");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, MAX_TIME);
    }

    //一段时间内未操作
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //退出
            HttpUtils.getInstance().logout(SPUtils.get().getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<String>(MainActivity.this, true) {
                        @Override
                        public void onSuccess(String t) {
                            //首先清空所有的用户数据
                            SPUtils spUtils = SPUtils.get();
                            spUtils.putBoolean(SPUtils.Config.IS_LOGIN, false);
                            spUtils.putString(SPUtils.Config.UID, "");
                            spUtils.putString(SPUtils.Config.TOKEN, null);
                            spUtils.putString(SPUtils.Config.USER, "{}");
                            spUtils.putString(SPUtils.Config.SHOP_ID, "");
                            showUserAuthOutDialog();
                        }

                        @Override
                        public void onFail(ExceptionReason msg) {

                        }
                    });
        }
    };
}
