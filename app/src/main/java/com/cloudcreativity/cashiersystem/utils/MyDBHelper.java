package com.cloudcreativity.cashiersystem.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作工具类
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "sggm_order_db";
    private static final int DB_VERSION = 1;
    //table name
    static final String TABLE_ORDER = "t_orders";
    static final String TABLE_GOODS = "t_goods";
    static final String TABLE_CATE = "t_category";

    //table t_orders column name
    static final String C_ID = "id";
    static final String C_MOBILE = "mobile";
    static final String C_MID = "mid";
    static final String C_TOTAL_MONEY = "total_money";
    static final String C_DISCOUNT_REDUCE = "discount_reduce";
    static final String C_MEMO = "memo";
    static final String C_DISCOUNT_MONEY = "discount_money";
    static final String C_ORDER_DETAILS = "order_details";

    //table t_goods column name
    static final String C_G_GOODS_ID = "goods_id";
    static final String C_G_GOODS_NAME = "goods_name";
    static final String C_G_STANDARDS = "standards";
    static final String C_G_UNIT = "unit";
    static final String C_G_PRICE = "price";
    static final String C_G_LABEL = "label";
    static final String C_G_STOCK = "stock";
    static final String C_G_CATE_ONE = "one_id";
    static final String C_G_CATE_TWO = "two_id";
    static final String C_G_RATE = "rate";
    static final String C_G_CODE = "code";

    //table t_category column name
    static final String C_C_CONTENT = "content";

    private static MyDBHelper instance;

    public synchronized static MyDBHelper getInstance(Context context){
        if(instance==null){
            synchronized (MyDBHelper.class){
                if(instance==null){
                    instance = new MyDBHelper(context);
                }
            }
        }
        return instance;
    }

    private MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 数据库第一次使用时，才会创建和执行
     * @param db 数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_order = "CREATE TABLE IF NOT EXISTS "+TABLE_ORDER+" (" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "mobile VARCHAR(11)," +
                "mid BIGINT," +
                "total_money DOUBLE(9,2)," +
                "discount_reduce DOUBLE(9,2)," +
                "discount_money DOUBLE(9,2)," +
                "order_details VARCHAR(1000)," +
                "memo VARCHAR(255)," +
                "create_time DATETIME" +
                ")";
        String create_goods = "CREATE TABLE IF NOT EXISTS "+TABLE_GOODS+" (" +
                "goods_id INTEGER," +
                "goods_name VARCHAR(100)," +
                "standards VARCHAR(20)," +
                "unit VARCHAR(10)," +
                "price INTEGER," +
                "label VARCHAR(255)," +
                "stock DOUBLE(9,2)," +
                "one_id VARCHAR(10)," +
                "two_id VARCHAR(10)," +
                "rate INTEGER," +
                "code VARCHAR(20))";
        String create_category = "CREATE TABLE IF NOT EXISTS "+TABLE_CATE+" (" +
                "content varchar)";
        db.execSQL(create_order);
        db.execSQL(create_goods);
        db.execSQL(create_category);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
