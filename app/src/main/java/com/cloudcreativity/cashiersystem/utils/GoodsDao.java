package com.cloudcreativity.cashiersystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudcreativity.cashiersystem.entity.DownloadGoodsData;
import com.cloudcreativity.cashiersystem.entity.GoodsEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 */
public class GoodsDao {
    private static GoodsDao INSTANCE = null;
    private SQLiteDatabase db;
    private MyDBHelper dbHelper;

    private GoodsDao(Context context) {
        dbHelper = MyDBHelper.getInstance(context);
    }

    public static GoodsDao getInstance(Context context) {
        if (INSTANCE == null) {
            return INSTANCE = new GoodsDao(context);
        }
        return INSTANCE;
    }

    /**
     * 添加订单信息
     *
     * @param entity 订单信息
     */
    public void addGoods(List<DownloadGoodsData> entity) {
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values;
            db.beginTransaction();
            for (DownloadGoodsData item : entity) {
                values = new ContentValues();
                values.put(MyDBHelper.C_G_GOODS_NAME, item.getGoodsName());
                values.put(MyDBHelper.C_G_GOODS_ID, item.getGoodsId());
                values.put(MyDBHelper.C_G_STANDARDS, item.getStandards());
                values.put(MyDBHelper.C_G_UNIT, item.getUnit());
                values.put(MyDBHelper.C_G_PRICE, item.getPrice());
                values.put(MyDBHelper.C_G_LABEL, item.getTab());
                values.put(MyDBHelper.C_G_CATE_ONE, item.getCategoryOneId());
                values.put(MyDBHelper.C_G_CATE_TWO, item.getCategoryTwoId());
                values.put(MyDBHelper.C_G_RATE, item.getDiscountRate());
                values.put(MyDBHelper.C_G_STOCK,item.getComputerStock());
                values.put(MyDBHelper.C_G_CODE, item.getBarCode());
                db.insert(MyDBHelper.TABLE_GOODS, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        } catch (Exception e) {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * @param gid 商品id
     * @return 删除成功 删除成功返回 影响的行 删除失败 返回-1
     */
    public int deleteGoods(long gid) {
        try {
            db = dbHelper.getWritableDatabase();
            db.delete(MyDBHelper.TABLE_GOODS, "goods_id = ?",
                    new String[]{String.valueOf(gid)});
            db.close();
            return 1;
        } catch (Exception e) {
            db.close();
            return -1;
        }
    }

    /**
     *
     * @param gid 商品id
     * @param stock 库存减少
     * @return 成功返回>0 失败返回<=0
     */
    public int updateStock(long gid, double stock) {
        try {
            GoodsEntity goods = getGoodsById(gid);
            db = dbHelper.getWritableDatabase();
            if(goods!=null){
                ContentValues values = new ContentValues();
                values.put(MyDBHelper.C_G_STOCK,goods.getComputerStock()-stock);
                db.update(MyDBHelper.TABLE_GOODS,values,
                        "goods_id="+gid,null);
                db.close();
                return 1;
            }else{
                db.close();
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return -1;
        }
    }

    public void deleteAll() {
        try {
            db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE from " + MyDBHelper.TABLE_GOODS);
        } catch (Exception e) {
            db.close();
            e.printStackTrace();
        }
    }

    /**
     *
     * @param gid 商品id
     * @return 商品
     */
    public GoodsEntity getGoodsById(long gid) {
        try {
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(MyDBHelper.TABLE_GOODS, new String[]{},
                    "goods_id = ?", new String[]{String.valueOf(gid)},
                    null, null,
                    null);
            cursor.moveToFirst();
            if(cursor.getCount()<=0) return null;
            // firstly, get column id by column name
            int nameIndex = cursor.getColumnIndex(MyDBHelper.C_G_GOODS_NAME);
            int idIndex = cursor.getColumnIndex(MyDBHelper.C_G_GOODS_ID);
            int standardsIndex = cursor.getColumnIndex(MyDBHelper.C_G_STANDARDS);
            int unitIndex = cursor.getColumnIndex(MyDBHelper.C_G_UNIT);
            int priceIndex = cursor.getColumnIndex(MyDBHelper.C_G_PRICE);
            int stockIndex = cursor.getColumnIndex(MyDBHelper.C_G_STOCK);
            int labelIndex = cursor.getColumnIndex(MyDBHelper.C_G_LABEL);
            int cateOneIndex = cursor.getColumnIndex(MyDBHelper.C_G_CATE_ONE);
            int cateTwoIndex = cursor.getColumnIndex(MyDBHelper.C_G_CATE_TWO);
            int rateIndex = cursor.getColumnIndex(MyDBHelper.C_G_RATE);
            int codeIndex = cursor.getColumnIndex(MyDBHelper.C_G_CODE);

            // secondly, judge cursor is could move to the first and has the next element
            // according column index to get value
            String name = cursor.getString(nameIndex);
            long goodsId = cursor.getLong(idIndex);
            String standards = cursor.getString(standardsIndex);
            String unit = cursor.getString(unitIndex);
            String label = cursor.getString(labelIndex);
            int price = cursor.getInt(priceIndex);
            double stock = cursor.getDouble(stockIndex);
            String cateOne = cursor.getString(cateOneIndex);
            String cateTwo = cursor.getString(cateTwoIndex);
            int rate = cursor.getInt(rateIndex);
            String code = cursor.getString(codeIndex);

            //构造数据，将数据添加在list返回
            GoodsEntity.GoodsDomain gd = new GoodsEntity.GoodsDomain(goodsId, name, cateOne, cateTwo,
                    label, standards, unit, price, code);
            GoodsEntity ge = new GoodsEntity(cateOne, cateTwo, stock, goodsId, gd, name, rate);
            cursor.close();
            db.close();
            return ge;
        } catch (Exception e) {
            // catch exception return null
            db.close();
            return null;
        }
    }

    /**
     * 查询全部的挂单记录
     *
     * @param cateTwoId 二级分类id
     * @return 返回特殊的数据格式
     */
    public List<GoodsEntity> queryGoods(String cateTwoId,int page,int size) {
        try {
            db = dbHelper.getWritableDatabase();
            int start = 0;

            if(page>1){
                start = 15;
            }

            Cursor cursor = db.query(MyDBHelper.TABLE_GOODS, new String[]{},
                    "two_id = ?", new String[]{cateTwoId},
                    null, null,
                    null,start+","+size);
            cursor.moveToFirst();
            List<GoodsEntity> data = new ArrayList<>();

            // firstly, get column id by column name
            int nameIndex = cursor.getColumnIndex(MyDBHelper.C_G_GOODS_NAME);
            int idIndex = cursor.getColumnIndex(MyDBHelper.C_G_GOODS_ID);
            int standardsIndex = cursor.getColumnIndex(MyDBHelper.C_G_STANDARDS);
            int unitIndex = cursor.getColumnIndex(MyDBHelper.C_G_UNIT);
            int priceIndex = cursor.getColumnIndex(MyDBHelper.C_G_PRICE);
            int stockIndex = cursor.getColumnIndex(MyDBHelper.C_G_STOCK);
            int labelIndex = cursor.getColumnIndex(MyDBHelper.C_G_LABEL);
            int cateOneIndex = cursor.getColumnIndex(MyDBHelper.C_G_CATE_ONE);
            int cateTwoIndex = cursor.getColumnIndex(MyDBHelper.C_G_CATE_TWO);
            int rateIndex = cursor.getColumnIndex(MyDBHelper.C_G_RATE);
            int codeIndex = cursor.getColumnIndex(MyDBHelper.C_G_CODE);

            // secondly, judge cursor is could move to the first and has the next element
            while (!cursor.isAfterLast()) {
                // according column index to get value
                String name = cursor.getString(nameIndex);
                long goodsId = cursor.getLong(idIndex);
                String standards = cursor.getString(standardsIndex);
                String unit = cursor.getString(unitIndex);
                String label = cursor.getString(labelIndex);
                int price = cursor.getInt(priceIndex);
                double stock = cursor.getDouble(stockIndex);
                String cateOne = cursor.getString(cateOneIndex);
                String cateTwo = cursor.getString(cateTwoIndex);
                int rate = cursor.getInt(rateIndex);
                String code = cursor.getString(codeIndex);

                //构造数据，将数据添加在list返回
                GoodsEntity.GoodsDomain gd = new GoodsEntity.GoodsDomain(goodsId, name, cateOne, cateTwo,
                        label, standards, unit, price, code);
                GoodsEntity ge = new GoodsEntity(cateOne, cateTwo, stock, goodsId, gd, name, rate);

                data.add(ge);
                cursor.moveToNext();
            }
            // finally close cursor
            cursor.close();
            db.close();
            return data;
        } catch (Exception e) {
            // catch exception return null
            db.close();
            return null;
        }
    }
}
