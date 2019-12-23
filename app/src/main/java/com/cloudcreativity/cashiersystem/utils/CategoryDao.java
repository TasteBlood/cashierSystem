package com.cloudcreativity.cashiersystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作
 */
public class CategoryDao {
    private static CategoryDao INSTANCE = null;
    private SQLiteDatabase db;
    private MyDBHelper dbHelper;

    private CategoryDao(Context context) {
        dbHelper = MyDBHelper.getInstance(context);
    }

    public static CategoryDao getInstance(Context context) {
        if (INSTANCE == null) {
            return INSTANCE = new CategoryDao(context);
        }
        return INSTANCE;
    }

    /**
     * 添加分类信息
     * @param content 分类信息 json数据
     */
    public void addCate(String content) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MyDBHelper.C_C_CONTENT,content);
            db.insert(MyDBHelper.TABLE_CATE,null,values);
            db.close();
        } catch (Exception e) {
            db.close();
        }
    }

    public String getCateContent() {
        try {
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(MyDBHelper.TABLE_CATE, new String[]{},
                    "", null,
                    null, null,
                    null);
            cursor.moveToFirst();
            if(cursor.getCount()<=0) return null;
            // firstly, get column id by column name
            int nameIndex = cursor.getColumnIndex(MyDBHelper.C_C_CONTENT);
            String content = cursor.getString(nameIndex);
            cursor.close();
            db.close();
            return content;
        } catch (Exception e) {
            // catch exception return null
            db.close();
            return "";
        }
    }

    public void deleteAll() {
        try {
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE from " + MyDBHelper.TABLE_CATE);
            db.close();
        } catch (Exception e) {
            db.close();
            e.printStackTrace();
        }
    }
}
