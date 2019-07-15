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

    /**
     * 打印机的生产厂商ID
     */
    public static final int PRINTER_VENDOR_ID = 34918;

    public static  class FRAGMENT_NAMES{
        public static final String FRAGMENT_ORDER = "fragment_order";
        public static final String FRAGMENT_OPEN_ORDER = "fragment_open_order";
        public static final Object FRAGMENT_CASHIER = "fragment_cashier";
        public static final String FRAGMENT_ORDER_DETAIL = "fragment_order_detail";
        public static final String FRAGMENT_ORDER_LIST = "fragment_order_list";
        public static final String FRAGMENT_MEMBER_LIST = "fragment_order_list";
        public static final String FRAGMENT_MEMBER_DETAIL = "fragment_member_detail";
        public static final String FRAGMENT_MEMBER_DETAIL_INDEX = "fragment_member_detail_index";
        public static final String FRAGMENT_MEMBER_PAY = "fragment_member_pay";
        public static final String FRAGMENT_MEMBER_BALANCE = "fragment_member_balance";
        public static final String FRAGMENT_MEMBER_SCORE = "fragment_member_score";
        public static final String FRAGMENT_MEMBER_EDIT = "fragment_member_edit";
        public static final String FRAGMENT_PERSONAL_CHANGE_PWD = "fragment_personal_change_pwd";
        public static final String FRAGMENT_PERSONAL_DEFAULT = "fragment_personal_default";
        public static final String FRAGMENT_PERSONAL_CHANGE_MOBILE = "fragment_personal_change_mobile";
        public static final String FRAGMENT_MESSAGE_LIST = "fragment_message_list";
        public static final String FRAGMENT_PERSONAL_LOG = "fragment_personal_log";
    }

    public interface  PAY_WAY{
        int PAY_BALANCE = 1;
        int PAY_MONEY = 2;
        int PAY_WX = 3;
        int PAY_ALI = 4;
        int PAY_UN = 5;
        int PAY_OTHER = 6;

    }

}
