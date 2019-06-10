package com.cloudcreativity.cashiersystem.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class FontUtils {
    public static void changeFont(Context context,String fontPath){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),fontPath);
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
