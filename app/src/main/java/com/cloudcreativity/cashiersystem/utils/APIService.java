package com.cloudcreativity.cashiersystem.utils;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 整个程序的网络接口配置
 */
public interface APIService {
    /**
     * 网络请求的配置
     */
    long timeOut = 10;//网络超时
    /**
     * 整体的接口配置
     */
    String TEST_HOST = "http://192.168.31.51:8083/";
    String ONLINE_HOST = "http://service.milidianshang.cn/";
    String HOST_APP = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;
    String VERIFY_CODE = HOST_APP +"/basics/getCode";

    @GET("/app/getCategory")
    Observable<String> getCategory();

    @POST("/basics/login")
    @FormUrlEncoded
    Observable<String> login(@Field("mobile") String mobile,
                             @Field("password") String password);

    @GET("/basics/logout")
    Observable<String> logout(@Query("adminId") String adminId);

    @GET("/app/getGoods")
    Observable<String> getGoods(@Query("pageNum") int pageNum,
                                @Query("pageSize") int pageSize,
                                @Query("categoryTwoId") String categoryTwo);

    @GET("/app/queryMessage")
    Observable<String> getNews(@Query("pageNum") int page,
                               @Query("pageSize") int size,
                               @Query("category") Integer category,
                               @Query("type") Integer state);

    @GET("/basics/getLoginLog")
    Observable<String> getLoginLog(@Query("pageNum") int page,
                                   @Query("pageSize") int size,
                                   @Query("timeKey") String timeKey,
                                   @Query("adminId") String adminId);

    @POST("/basics/editPwd")
    @FormUrlEncoded
    Observable<String> changePwd(@Field("adminId") String adminId,
                                 @Field("password") String pwd1,
                                 @Field("confirmPwd") String pwd2,
                                 @Field("msgCode") String msgCode);

    @POST("/app/editMobile")
    @FormUrlEncoded
    Observable<String> changeMobile(@Field("mobile") String mobile,
                                    @Field("msgCode") String code,
                                    @Field("password") String password,
                                    @Field("oldMobile") String oldPhone);

    @POST("/basics/forgetPwd")
    Observable<String> forgetPwd(@Field("mobile") String mobile,
                                 @Field("msgCode") String code,
                                 @Field("password") String password);

    @GET("/basics/getMaxVersion")
    Observable<String> getLastVersion();
}
