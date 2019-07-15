package com.cloudcreativity.cashiersystem.utils;

import android.view.View;

public class SystemUIUtils {
    /**
     * 隐藏虚拟按键，并且全屏
     * getWindow().getDecorView()
     */
    public static void setStickFullScreen(View v) {
        //隐藏虚拟按键，并且全屏
            //for new api versions.
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            v.setSystemUiVisibility(uiOptions);
    }
}