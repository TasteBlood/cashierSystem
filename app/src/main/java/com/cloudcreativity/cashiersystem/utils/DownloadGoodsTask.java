package com.cloudcreativity.cashiersystem.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.cloudcreativity.cashiersystem.R;
import com.cloudcreativity.cashiersystem.activity.MainActivity;
import com.cloudcreativity.cashiersystem.base.BaseDialogImpl;
import com.cloudcreativity.cashiersystem.entity.DownloadGoodsData;
import com.cloudcreativity.cashiersystem.entity.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

/**
 * 下载商品信息线程
 */
public class DownloadGoodsTask extends AsyncTask<Void,Integer,String> {

    private UserEntity userEntity;
    @SuppressLint("StaticFieldLeak")
    private Activity context;
    private BaseDialogImpl baseDialog;
    private Dialog dialog;
    @SuppressLint("StaticFieldLeak")
    private TextView tv_result;

    public DownloadGoodsTask(UserEntity userEntity, Activity context,BaseDialogImpl baseDialog) {
        this.userEntity = userEntity;
        this.context = context;
        this.baseDialog = baseDialog;
        SPUtils.get().putString(SPUtils.Config.USER,new Gson().toJson(userEntity));
        SPUtils.get().putString(SPUtils.Config.TOKEN,userEntity.getToken());
        SPUtils.get().putString(SPUtils.Config.UID,userEntity.getId());
        SPUtils.get().putBoolean(SPUtils.Config.IS_LOGIN,true);
        SPUtils.get().putString(SPUtils.Config.SHOP_ID,userEntity.getShopId());
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(context, R.style.myProgressDialogStyle);
        View root = View.inflate(context,R.layout.layout_dialog_download_goods,null);
        tv_result = root.findViewById(R.id.tv_result);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(root);
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpUtils.getInstance().downloadGoods(userEntity.getShopId())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new DefaultObserver<String>(baseDialog,false) {
                    @Override
                    public void onSuccess(String t) {
                        try {
                            JSONObject object = new JSONObject(t);
                            String inventoryList = object.getString("inventoryList");
                            Type type = new TypeToken<List<DownloadGoodsData>>() {
                            }.getType();
                            List<DownloadGoodsData> goodsEntities = new Gson().fromJson(inventoryList,type);
                            // 将数据添加到数据库，先删除原来的数据
                            GoodsDao.getInstance(context).deleteAll();
                            GoodsDao.getInstance(context).addGoods(goodsEntities);
                            // 将分类数据添加到数据库
                            String categoryString = object.getString("shop");
                            CategoryDao.getInstance(context).deleteAll();
                            CategoryDao.getInstance(context).addCate(categoryString);

                            onPostExecute("finish");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onPostExecute("error");
                        }

                    }

                    @Override
                    public void onFail(ExceptionReason msg) {
                        onPostExecute("error");
                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        if(s==null) return;

        if("finish".equals(s)){
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_result.setText("数据同步完成，即将进入系统");
                    if(dialog!=null)
                        dialog.dismiss();
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                    cancel(true);
                }
            });

        }else if("error".equals(s)){
            SPUtils.get().putBoolean(SPUtils.Config.IS_LOGIN,false);
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_result.setText("数据同步失败，请重新登录");
                    if(dialog!=null)
                        dialog.dismiss();
                }
            });
            cancel(true);
        }


    }
}
