package com.cloudcreativity.cashiersystem.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cloudcreativity.cashiersystem.R;

public class FGUtils {
    /**
     *
     * @param manager fg 管理器
     * @param container 内容
     * @param target 目标
     */
    public static void replace(FragmentManager manager, int container, Fragment target){
        manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in,
                        R.anim.slide_left_out,
                        R.anim.slide_left_in,
                        R.anim.slide_right_out)
                .replace(container,target)
                .commit();
    }

    public static void replaceNoAnim(FragmentManager manager, int container, Fragment target){
        manager.beginTransaction()
                .replace(container,target)
                .commit();
    }
}
