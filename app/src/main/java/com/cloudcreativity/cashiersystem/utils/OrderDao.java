package com.cloudcreativity.cashiersystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudcreativity.cashiersystem.entity.ListEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 */
public class OrderDao {
    private static OrderDao INSTANCE = null;
    private SQLiteDatabase db;
    private MyDBHelper dbHelper;

    private OrderDao(Context context){
        dbHelper = MyDBHelper.getInstance(context);
    }

    public static OrderDao getInstance(Context context){
        if(INSTANCE == null){
            return INSTANCE  = new OrderDao(context);
        }
        return INSTANCE;
    }

    /**
     * 添加订单信息
     * @param entity 订单信息
     * @return 添加成功 返回添加成功的id 否则返回-1
     */
    public long addOrder(ListEntity entity){
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("mobile",entity.getMobile());
            contentValues.put("mid",entity.getMid());
            contentValues.put("total_money",entity.getTotalMoney());
            contentValues.put("discount_reduce",entity.getDiscountReduce());
            contentValues.put("discount_money",entity.getDiscountMoney());
            contentValues.put("memo",entity.getMemo());
            contentValues.put("order_details",new Gson().toJson(entity.getItems()));
            contentValues.put("create_time","now()");
            long result = db.insert(MyDBHelper.TABLE_ORDER,null,contentValues);
            db.close();
            return result;
        }catch (Exception e){
            db.close();
            return -1;
        }
    }

    /**
     * @param oid 订单id
     * @return 删除成功 删除成功返回 影响的行 删除失败 返回-1
     */
    public int deleteOrder(long oid){
        try {
            db = dbHelper.getWritableDatabase();
            db.delete(MyDBHelper.TABLE_ORDER,"id = ?",new String[]{String.valueOf(oid)});
            db.close();
            return 1;
        }catch (Exception e){
            db.close();
            return -1;
        }
    }

    public void deleteAll(){
        try {
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE from "+MyDBHelper.TABLE_ORDER);
            db.close();
        }catch (Exception e){
            db.close();
            e.printStackTrace();
        }
    }

    /**
     * 查询全部的挂单记录
     * order_details 是json字符串 需要格式化才能使用
     * @return 返回特殊的数据格式
     */
    public List<Map<String,Object>> queryOrders(){
        try{
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(MyDBHelper.TABLE_ORDER, new String[]{},
                    null, null,
                    null, null,
                    "create_time desc");
            cursor.moveToFirst();
            List<Map<String,Object>> data = new ArrayList<>();

            // firstly, get column id by column name
            int id = cursor.getColumnIndex(MyDBHelper.C_ID);
            int mobile = cursor.getColumnIndex(MyDBHelper.C_MOBILE);
            int mid = cursor.getColumnIndex(MyDBHelper.C_MID);
            int discount_money = cursor.getColumnIndex(MyDBHelper.C_DISCOUNT_MONEY);
            int discount_reduce = cursor.getColumnIndex(MyDBHelper.C_DISCOUNT_REDUCE);
            int total_money = cursor.getColumnIndex(MyDBHelper.C_TOTAL_MONEY);
            int memo = cursor.getColumnIndex(MyDBHelper.C_MEMO);
            int order_details = cursor.getColumnIndex(MyDBHelper.C_ORDER_DETAILS);

            // secondly, judge cursor is could move to the first and has the next element
            while(!cursor.isAfterLast()){
                // according column index to get value
                long idData = cursor.getLong(id);
                String mobileData = cursor.getString(mobile);
                double disMoney = cursor.getDouble(discount_money);
                double disReduce = cursor.getDouble(discount_reduce);
                double totalMoney = cursor.getDouble(total_money);
                String orderDetails = cursor.getString(order_details);
                String memoStr = cursor.getString(memo);
                long midLong = cursor.getLong(mid);

                // thirdly, put data into the map<String,Object>
                Map<String,Object> item = new HashMap<>();
                item.put("id",idData);
                item.put("mid",midLong);
                item.put("memo",memoStr);
                item.put("mobile",mobileData);
                item.put("totalMoney",totalMoney);
                item.put("discountMoney",disMoney);
                item.put("discountReduce",disReduce);
                item.put("orderDetails",orderDetails);

                data.add(item);

                cursor.moveToNext();
            }
            // finally close cursor
            cursor.close();
            db.close();
            return data;
        }catch (Exception e){
            // catch exception return null
            db.close();
            return null;
        }
    }
}
