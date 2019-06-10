package com.cloudcreativity.cashiersystem.utils;

/**
 * 这个app的属性配置
 */
public class AppConfig {
    /**
     * 是否是开发调试阶段
     */
    public static boolean DEBUG = true;
    /**
     * 网络数据缓存的文件夹名称
     */
    public static final String CACHE_FILE_NAME = "app_cache";
    /**
     * 网络图片缓存的文件夹名称
     */
    public static final String CACHE_IMAGE_NAME = "app_image_cache";
    /**
     * 这是SharePreference的名称
     */
    public static final String SP_NAME = "cashier_system_config";

    /**
     * 这是统一的文件名
     */
    public static String FILE_NAME = "cashier_system_image_%d.%s";

    /**
     * 这是APP热更新的下载缓存文件名
     */
    static String APP_HOT_UPDATE_FILE = "cashier_system_hot_update.apk";

    public static  class FRAGMENT_NAMES{
        public static final String FRAGMENT_ORDER = "fragment_order";
        public static final String FRAGMENT_OPEN_ORDER = "fragment_open_order";
        public static final Object FRAGMENT_CASHIER = "fragment_cashier";
    }

}
