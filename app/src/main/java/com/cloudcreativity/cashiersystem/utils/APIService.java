package com.cloudcreativity.cashiersystem.utils;

import com.cloudcreativity.cashiersystem.entity.OpenOrderGoodsEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @GET("/basics/queryMessage")
    Observable<String> getNews(@Query("pageNum") int page,
                               @Query("pageSize") int size,
                               @Query("category") Integer category,
                               @Query("type") Integer state);

    @GET("/basics/findMessage")
    Observable<String> queryNews(@Query("id") int nid);

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

    @GET("/app/findGoodsByCode")
    Observable<String> queryGoods(@Query("barCode") String code);

    @GET("/app/queryMember")
    Observable<String> getMembers(@Query("identity") Integer identity,
                                  @Query("level") Integer level,
                                  @Query("pageNum") int page,
                                  @Query("pageSize") int size,
                                  @Query("param") String param);

    @POST("/app/addMember")
    @FormUrlEncoded
    Observable<String> addMember(@Field("identity") Integer identity,
                                 @Field("mobile") String mobile);

    @GET("/app/findMember")
    Observable<String> queryMember(@Query("id") long mid);

    @POST("/app/editMember")
    @FormUrlEncoded
    Observable<String> editMember(@Field("memberId") long mid,
                                  @Field("mobile") String mobile);

    @POST("/app/addOrder")
    @FormUrlEncoded
    Observable<String> submitOrder(@Field("discountMoney") int discountMoney,
                                   @Field("integralMoney") int integralMoney,
                                   @Field("memberId") long memberId,
                                   @Field("mobile") String mobile,
                                   @Field("payMoney") int payMoney,
                                   @Field("payId") int payId,
                                   @Field("totalMoney") int totalMoney,
                                   @Field("finalMoney") int finalMoney,
                                   @Field("type") int type,
                                   @Field("zeroMoney") int zeroMoney,
                                   @Field("remark") String remark,
                                   @Field("orderDetailDomains")String goods);

//    @POST("/app/addOrder")
//    @Headers("Content-type: application/json")
//    Observable<String> submitOrder(@Body RequestBody body);

    @GET("/app/selectOrder")
    Observable<String> getOrders(@Query("dateTime") String dateTime,
                                 @Query("identity") Integer identity,
                                 @Query("param") String param,
                                 @Query("payWay") Integer payWay,
                                 @Query("timeType") Integer timeType,
                                 @Query("pageNum") int pageNum,
                                 @Query("pageSize") int pageSize);

    @POST("/app/selectOrderDetail")
    @FormUrlEncoded
    Observable<String> queryOrder(@Field("orderId") int orderId);

    @GET("/basics/getMaxVersion")
    Observable<String> getLastVersion();

    @POST("/app/selectMemberIdByPhone")
    @FormUrlEncoded
    Observable<String> queryMemberByPhone(@Field("phoneNum") String phone);
}
