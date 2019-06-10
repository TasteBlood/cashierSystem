package com.cloudcreativity.cashiersystem.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.cloudcreativity.cashiersystem.base.BaseApp;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * 自定义的网络请求框架，采用Retrofit+RxJava+OkHttp
 * description:
 * 1: 实现了自定义的错误处理方式。
 * 2：实现了缓存，有网络时在一定的时间内加载缓存，没有网络时直接加载缓存的内容。
 * 3：实现自定义OkHttp，可以在请求时加入统一参数，比如用户token和appKey等。
 */
public class HttpUtils {
    private APIService apiService;

    private HttpUtils() {
        LoggingInterceptor interceptor = new LoggingInterceptor();
        //初始化缓存
        File cacheFile = new File(BaseApp.app.getExternalCacheDir(), AppConfig.CACHE_FILE_NAME);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);//100M的缓存
        //初始化OkHttp
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(APIService.timeOut, TimeUnit.SECONDS)
                .connectTimeout(APIService.timeOut, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder().client(client)
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(APIService.HOST_APP)
                .build();
        apiService = retrofit.create(APIService.class);
    }

    //创建单利模式
    private static class SingleHolder {
        private static final HttpUtils UTILS = new HttpUtils();
    }

    //获取实例
    public static APIService getInstance() {
        return SingleHolder.UTILS.apiService;
    }

    //网络缓存
    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isAvailable()) {  //没网强制从缓存读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d(HttpUtils.this.getClass().getName(), "no network");
            }

            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isAvailable()) {
                int maxAge = 0;// 有网，1个小时可用
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxStale = 60 * 60 * 6;// 没网 就6小时可用
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    public static void uploadImage(String token, String name, String idCardNumber, File file, String url, final Activity context, final OnSuccessListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("name", name);
        params.put("idCardNumber", idCardNumber);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //LogUtils.e("xuxiwu",file.getAbsolutePath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg;charset=utf-8"), file);
        builder.addFormDataPart("file", file.getName(), requestBody);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            builder.addFormDataPart(stringObjectEntry.getKey(), (String) stringObjectEntry.getValue());
        }
        //初始化OkHttp
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(APIService.timeOut, TimeUnit.SECONDS)
                .connectTimeout(APIService.timeOut, TimeUnit.SECONDS)
                .build();

        // request方法： @param [String]URL, [RequestBody]requestBody
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {

            private String string;

            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    string = response.body().string();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(string);
                        }
                    });
                } else {
                    final String toString = response.body().string();
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(toString);
                        }
                    });
                }
            }
        });
    }

    public static interface OnSuccessListener {
        public void onSuccess(String response);

        public void onError(String error);
    }
}
