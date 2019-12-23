package com.cloudcreativity.cashiersystem.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.google.gson.Gson;

/**
 * 这是SharePreferences工具
 */
public class SPUtils {

    public interface Config{
        String IS_LOGIN = "app_is_login";
        String SMS_TIME = "app_sms_time";
        String UID = "app_login_user_id";
        String TOKEN = "app_request_token";
        String USER = "app_login_user";
        String SHOP_ID = "shop_id";
    }

    private static SharedPreferences preferences;
    private static SPUtils utils;
    private SPUtils(){
        preferences = BaseApp.app.getSharedPreferences(AppConfig.SP_NAME, Context.MODE_PRIVATE);
    }

    public synchronized static SPUtils get(){
        return utils==null?utils=new SPUtils():utils;
    }

    public  void putString(String name, String value){
        preferences.edit().putString(name,value).apply();
    }
    public  void putInt(String name, int value){
        preferences.edit().putInt(name,value).apply();
    }

    public void putBoolean(String name, boolean value){
        preferences.edit().putBoolean(name,value).apply();
    }

    public boolean getBoolean(String name, boolean defaultValue){
        return preferences.getBoolean(name,defaultValue);
    }

    public String getString(String name, String defaultValue){
        return preferences.getString(name,defaultValue);
    }

    public  int getInt(String name, int defaultValue){
        return preferences.getInt(name,defaultValue);
    }

    //移除保存的数据
    public  void remove(String name){
        preferences.edit().remove(name).apply();
    }

    public boolean isLogin(){
        return preferences.getBoolean(Config.IS_LOGIN,false);
    }

    public String getToken() {
        return preferences.getString(Config.TOKEN,"");
    }

    public String
    getUid() {
        return preferences.getString(Config.UID,"");
    }

    public String getShopId(){return preferences.getString(Config.SHOP_ID,"");}
    public UserEntity getUser() {
        return new Gson().fromJson(preferences.getString(Config.USER,"{}"),UserEntity.class);
    }

    public void setSMSTime(long time){
        preferences.edit().putLong(Config.SMS_TIME,time).apply();
    }

    public long getSMSTime(){
        return preferences.getLong(Config.SMS_TIME,0);
    }
}
