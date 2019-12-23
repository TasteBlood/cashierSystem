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
    long timeOut = 20;//网络超时
    /**
     * 整体的接口配置
     */
    String TEST_HOST = "https://service.freshergo.com/";
//    String TEST_HOST = "http://192.168.31.119:8083/";
    String ONLINE_HOST = "http://service.milidianshang.cn/";
    String HOST_APP = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;
    // String VERIFY_CODE = HOST_APP +"/basics/getCode";

    @GET("/app/getCategory")
    Observable<String> getCategory();

    /**
     * 发送短信验证码
     * @param mobile 手机号
     */
    @POST("/basics/sendSms")
    @FormUrlEncoded
    Observable<String> sendSms(@Field("mobile") String mobile);

    /**
     * 检查短信验证码 是否可用
     * @param mobile 手机号
     * @param sms 短信验证码
     */
    @POST("/basics/unused")
    @FormUrlEncoded
    Observable<String> checkSms(@Field("mobile") String mobile,
                                @Field("sms") String sms);

    @POST("/basics/login")
    @FormUrlEncoded
    Observable<String> login(@Field("mobile") String mobile,
                             @Field("password") String password,
                             @Field("type") int type);

    @GET("/basics/getImgCode")
    Observable<String> getImageCode();

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
    @FormUrlEncoded
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

    @GET("/basics/getRechargeLogByMemberId")
    Observable<String> queryMemberLog(@Query("memberId") long mid,
                                      @Query("pageNum") int page,
                                      @Query("pageSize") int size);

    @GET("/basics/getCashLog")
    Observable<String> queryMemberPay(@Query("memberId") long mid,
                                      @Query("pageNum") int page,
                                      @Query("pageSize") int size);

    @POST("/app/findByConsume")
    @FormUrlEncoded
    Observable<String> queryMemberPayDetail(@Field("consumeId") int mid);


    @POST("/app/addMember")
    @FormUrlEncoded
    Observable<String> addMember(@Field("identity") Integer identity,
                                 @Field("mobile") String mobile);

    /**
     * 会员充值
     * @param mid 会员id
     * @param money 充值金额
     * @param payWay 支付方式 现金 手机
     * @param adminId 管理员id
     */
    @POST("/app/addRecharge")
    @FormUrlEncoded
    Observable<String> recharge(@Field("memberId") long mid,
                                @Field("money") int money,
                                @Field("payWayId") int payWay,
                                @Field("mobile") String mobile,
                                @Field("adminId") String adminId,
                                @Field("authCode") String code);

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
                                   @Field("orderDetailDomains")String goods,
                                   @Field("authCode") String authCode);

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


    @GET("/basics/getCashLog")
    Observable<String> getCashLog(@Query("adminId") long adminId,
                                  @Query("loginTime") String loginTime,
                                  @Query("logoutTime") String logoutTime,
                                  @Query("pageNum") int page,
                                  @Query("pageSize") int size);

    @GET("/basics/getAddMemberLog")
    Observable<String> getCreateLog(@Query("adminId") long adminId,
                                  @Query("loginTime") String loginTime,
                                  @Query("logoutTime") String logoutTime,
                                  @Query("pageNum") int page,
                                  @Query("pageSize") int size);

    @GET("/basics/getEditMemberLog")
    Observable<String> getEditLog(@Query("adminId") long adminId,
                                    @Query("loginTime") String loginTime,
                                    @Query("logoutTime") String logoutTime,
                                    @Query("pageNum") int page,
                                    @Query("pageSize") int size);

    @GET("/basics/getRechargeLog")
    Observable<String> getRechargeLog(@Query("adminId") long adminId,
                                  @Query("loginTime") String loginTime,
                                  @Query("logoutTime") String logoutTime,
                                  @Query("pageNum") int page,
                                  @Query("pageSize") int size);

    @GET("/basics/getMaxVersion")
    Observable<String> getLastVersion();

    @POST("/app/selectMemberIdByPhone")
    @FormUrlEncoded
    Observable<String> queryMemberByPhone(@Field("phoneNum") String phone);


    @POST("/app/query")
    @FormUrlEncoded
    Observable<String> queryResult(@Field("orderNum") String orderNum,@Field("orderId")int oid);

    @POST("/app/reverse")
    @FormUrlEncoded
    Observable<String> backOrder(@Field("orderNum") String orderNum);

    @POST("/app/updateOrder")
    @FormUrlEncoded
    /**
     * payId 3 wx
     * payId 4 alipay
     * payId 5 other
     */
    Observable<String> updateOrder(@Field("orderId") int orderId,@Field("payId") int payId);

    /**
     * @param id 充值单id
     * 更新重置状态
     */
    @GET("/app/updateState")
    Observable<String> updateRecharge(@Query("id") int id);

    @FormUrlEncoded
    @POST("/app/categoryTwoAmount")
    Observable<String> getCate2Amout(@Field("categoryOneId") String categoryOneId,
                                     @Field("memberId") long memberId);

    @GET("/app/getAll")
    Observable<String> downloadGoods(@Query("shopId") String shopId);
}
