package com.cloudcreativity.cashiersystem.activity;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.cloudcreativity.cashiersystem.utils.ScanGunHelper;
import com.cloudcreativity.cashiersystem.utils.SoftKeyboardUtils;
import com.cloudcreativity.cashiersystem.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements ScanGunHelper.OnScanSuccessListener {

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
        //LogUtils.e("xuxiwu",event.toString());
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
        if(this.getCurrentFocus()!=null)
            //LogUtils.e("xuxiwu","颖仓软键盘...");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            handler.removeCallbacks(runnable);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            startAD();
            //隐藏软键盘

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(!"Virtual".equals(event.getDevice().getName())){
            LogUtils.e("xuxiwu",event.getDevice().toString());
            ScanGunHelper.getInstance().analysisKeyEvent(event,this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onSuccess(String barcode) {
        if(barcode!=null&&!TextUtils.isEmpty(barcode)){
            if(barcode.length()==13){
                //商品码
                Map<String,String> data = new HashMap<>();
                data.put("name","goodsCode");
                data.put("code",barcode);
                EventBus.getDefault().post(data);
            }else{
                //支付码
                if(barcode.length()==18){
                    //微信支付
                    Map<String,String> data = new HashMap<>();
                    data.put("name","payCode");
                    data.put("code",barcode);
                    EventBus.getDefault().post(data);
                }else{
                    //支付宝支付
                    Map<String,String> data = new HashMap<>();
                    data.put("name","payCode");
                    data.put("code",barcode);
                    EventBus.getDefault().post(data);
                }
            }
        }
    }
}
