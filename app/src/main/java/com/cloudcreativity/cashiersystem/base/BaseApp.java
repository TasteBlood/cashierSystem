package com.cloudcreativity.cashiersystem.base;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.cloudcreativity.cashiersystem.utils.CrashHandler;
import com.cloudcreativity.cashiersystem.utils.FontUtils;
import com.cloudcreativity.cashiersystem.utils.SPUtils;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class BaseApp extends Application {

    public static BaseApp app;
    private List<Activity> activities = new ArrayList<>();

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        this.activities.remove(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //进程杀死，清空登录信息
        SPUtils spUtils = SPUtils.get();
        spUtils.putBoolean(SPUtils.Config.IS_LOGIN, false);
        spUtils.putString(SPUtils.Config.UID, "");
        spUtils.putString(SPUtils.Config.TOKEN, null);
        spUtils.putString(SPUtils.Config.USER, "{}");
        spUtils.putString(SPUtils.Config.SHOP_ID, "");

        FontUtils.changeFont(app, "simhei.ttf");
        //CrashHandler.getInstance().init(app);
        initRefreshLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void destroyActivity() {
        //结束所有的Activity
        for (Activity activity : activities) {
            if (activity != null && !activity.isDestroyed())
                activity.finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onTerminate() {
        super.onTerminate();
        destroyActivity();
        //onDestroy
        System.exit(0);
    }

    //初始化刷新加载布局
    private void initRefreshLayout() {
        TwinklingRefreshLayout.setDefaultHeader("com.cloudcreativity.cashiersystem.view.ProgressLayout");
        TwinklingRefreshLayout.setDefaultFooter("com.lcodecore.tkrefreshlayout.footer.LoadingView");
    }
}
