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
    String TEST_HOST = "http://192.168.31.124:8088/";
    String ONLINE_HOST = "http://service.milidianshang.cn/";
    String HOST = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;
    String HOST_APP = AppConfig.DEBUG ? TEST_HOST : ONLINE_HOST;




    @POST("/app/workerLogin/login")
    @FormUrlEncoded
    Observable<String> login(@Field("idCardNumber") String idCardNum,
                             @Field("password") String password);

    @POST("/app/workerLogin/editPsw")
    @FormUrlEncoded
    Observable<String> editPwd(@Field("password") String oldPwd,
                               @Field("newPassword") String newPwd);


    @GET("/app/worker/getPageListOfPro")
    Observable<String> getProList(@Query("pageNum") int pageNum);


    @GET("/app/attendance/getTimeOfWorker")
        /*
         * @param cardTime  年月 2019-02
         */
    Observable<String> getCardByMonth(@Query("projectCode") String projectCode,
                                      @Query("wId") int wId,
                                      @Query("year") int year,
                                      @Query("month") int month);

    @GET("/app/attendance/getAttendanceByDate")
    /**
     * @param cardTime  年月日 2019-02-02
     */
    Observable<String> getCardByDay(
            @Query("wId") int wId,
            @Query("year") int year,
            @Query("month") int month,
            @Query("day") int day);

    @POST("/app/workerSalary/getPageList")
    @FormUrlEncoded
    Observable<String> getSalary(@Field("wId") int pid,
                                 @Field("pageNum") int page,
                                 @Field("pageSize") int size);

    @GET("test")
    Observable<String> getLastVersion();

    /**
     * @param test 因为后台是post方式，而不传参数会导致okhttp 报错，所以传入假参数
     */
    @GET("admin/areaCode/getPageList")
    Observable<String> getProvince(@Query("testParams") String test);

    @GET("admin/areaCode/getPageList")
    Observable<String> getCity(@Query("parentId") int provinceId);

    @GET("admin/areaCode/getPageList")
    Observable<String> getArea(@Query("parentId") int cityId);

    //获取工资分类
    @POST("app/common/getSalarys")
    @FormUrlEncoded
    Observable<String> getSalaryList(@Field("testParams") String params);

    //获取经验分类
    @POST("app/common/getExperiences")
    @FormUrlEncoded
    Observable<String> getExperiences(@Field("testParams") String params);

    /**
     * @param experiences 经验id
     * @param salaryId    工资id
     * @param cityId      市id
     * @param page        页数
     * @param size        页码
     */
    @POST("app/position/getList")
    @FormUrlEncoded
    Observable<String> getPositionList(@Field("experienceId") int experiences,
                                       @Field("salaryId") int salaryId,
                                       @Field("cityId") int cityId,
                                       @Field("pageNum") int page,
                                       @Field("pageSize") int size);

    /**
     * @param page 页数
     * @param size 大小
     *             查询法律列表
     */
    @GET("app/law/getPageList")
    Observable<String> getLawByPage(@Query("pageNum") int page,
                                    @Query("pageSize") int size);

    @GET("app/rotation/getPageList")
    Observable<String> getBanner();


    @GET("admin/getQiNiuToken")
    Observable<String> getQiNiuToken();

    @POST("app/complaint/add")
    @FormUrlEncoded
    Observable<String> addPass(@Field("content") String content,
                               @Field("voiceUrl") String voiceUrl,
                               @Field("imgUrl") String imgUrl,
                               @Field("workerId") int workerId);

    @GET("app/complaint/getList")
    Observable<String> passList(@Query("pageNum") int page,
                                @Query("pageSize") int size);
}
