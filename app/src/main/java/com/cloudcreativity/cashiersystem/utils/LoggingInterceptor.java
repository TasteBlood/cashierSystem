package com.cloudcreativity.cashiersystem.utils;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求的日志打印工具
 */
public class LoggingInterceptor implements Interceptor {
    private String TAG = this.getClass().getName();
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        String method=request.method();
        if("POST".equals(method)){
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                //在这里进行参数的重新组装
                FormBody.Builder builder = new FormBody.Builder();
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                    //重新添加参数
                    builder.addEncoded(body.encodedName(i), body.encodedValue(i));
                }
                if(!request.url().toString().contains("login")){
                    builder.addEncoded("token",SPUtils.get().getToken())
                            .addEncoded("shopId",SPUtils.get().getShopId())
                            .addEncoded("uid", SPUtils.get().getUid());
                    sb.append("token" + "=" + SPUtils.get().getToken() + ",");
                    sb.append("uid" + "=" + SPUtils.get().getUid() + ",");
                    sb.append("shopId" + "=" + SPUtils.get().getShopId() + ",");
                    //sb.append("idCardNumber" + "=" + SPUtils.get().getString(SPUtils.Config.IDCARD,"") + ",");
                }

                body = builder.build();
                // body = builder.build();
                sb.delete(sb.length() - 1, sb.length());
                LogUtils.e(TAG, "| RequestParams:{"+sb.toString()+"}");
                request = request.newBuilder().post(body).build();
            }
        }else if("GET".equals(method)){
            if(!request.url().toString().contains("login")&&!request.url().toString().contains("addFace")){
                HttpUrl httpUrl = request.url().newBuilder()
                        .addQueryParameter("token", SPUtils.get().getToken())
                        .addQueryParameter("uid", SPUtils.get().getUid())
                        .addQueryParameter("shopId", SPUtils.get().getShopId())
                        //.addQueryParameter("idCardNumber",SPUtils.get().getString(SPUtils.Config.IDCARD,""))
                        .build();
                request = request.newBuilder().url(httpUrl).build();
            }else{
                HttpUrl httpUrl = request.url().newBuilder()
                        .build();
                request = request.newBuilder().url(httpUrl).build();
            }
        }
        LogUtils.e(TAG,"\n");
        LogUtils.e(TAG,"----------Start----------------");
        LogUtils.e(TAG, "| "+request.toString());

        Response response = chain.proceed(request);

        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtils.e(TAG, "| Response:" + content);
        LogUtils.e(TAG,"----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
