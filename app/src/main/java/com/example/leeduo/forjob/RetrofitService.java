package com.example.leeduo.forjob;


import android.support.annotation.Nullable;

import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerJobCompanyBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerKeysCompanyBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerSalaryCityBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerSalaryCompanyBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerSalaryJobBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.AnalyzerKeysJobBean;
import com.example.leeduo.forjob.JsonBean.AnalyzerJsonBean.JsonUserInformationBean;
import com.example.leeduo.forjob.JsonBean.JsonCaptchaBean;
import com.example.leeduo.forjob.JsonBean.JsonCompaniesBean;
import com.example.leeduo.forjob.JsonBean.JsonLoginSuccessBean;
import com.example.leeduo.forjob.JsonBean.JsonPositionBean;
import com.example.leeduo.forjob.JsonBean.JsonPositionsBean;
import com.example.leeduo.forjob.JsonBean.JsonRecommendHit;
import com.example.leeduo.forjob.JsonBean.JsonSingleCompanyBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by LeeDuo on 2019/2/6.
 */
//网络请求接口
public interface RetrofitService {
    @POST("companies")
    Observable<JsonCompaniesBean> getCompaniesJsonString(@Query("pageNum") int page,
                                                         @Query("pageSize") int size,
                                                         @Query("companyShortName") @Nullable String shortName,
                                                         @Query("city") @Nullable String city,
                                                         @Query("financeStage") @Nullable String financeStage,
                                                         @Query("companySize") @Nullable String companySize,
                                                         @Query("industryField") @Nullable String industryField);

    @POST("company")
    Observable<JsonSingleCompanyBean> getSingleCompanyJsonString(@Query("companyId") int companyId);


    @POST("position")
    Observable<JsonPositionBean> getSinglePositionJsonString(@Query("jobId") int positionId);

    @POST("positions")
    Observable<JsonPositionsBean> getSingleCompanyPositionsJsonString(
            @Query("pageNum") int page,
            @Query("pageSize") int size,
            @Query("companyId") @Nullable Integer companyId,
            @Query("positionType") @Nullable String positionType,
            @Query("city") @Nullable String city,
            @Query("minSalary") @Nullable Integer minSalary,
            @Query("maxSalary") @Nullable Integer maxSalary,
            @Query("experience") @Nullable String experience);

    @POST("like/job")
    Observable<JsonRecommendHit[]> getRecommendPositions(@Query("title") String title,
                                                         @Query("city") String city,
                                                         @Query("size") int size);
    @POST("analyzer/job/company")
    Observable<AnalyzerJobCompanyBean[]> getAnalyzerJobCompanyBean( @Query("companyId") int companyId,
                                                                    @Query("size") int size);
    @POST("analyzer/salary/city")
    Observable<AnalyzerSalaryCityBean[]> getAnalyzerSalaryCityBean(@Query("city") String city,
                                                                   @Query("size") int size);
    @POST("analyzer/salary/company")
    Observable<AnalyzerSalaryCompanyBean[]> getAnalyzerSalaryCompanyBean(@Query("companyId") int companyId,
                                                                         @Query("size") int size);
    @POST("analyzer/salary/job")
    Observable<AnalyzerSalaryJobBean[]> getAnalyzerSalaryJobBean(@Query("title") String title,
                                                                 @Query("size") int size);
    @POST("analyzer/keys/company")
    Observable<AnalyzerKeysCompanyBean[]> getAnalyzerKeysCompanyBean(@Query("companyId") int companyId,
                                                                        @Query("size") int size);
    @POST("analyzer/keys/job")
    Observable<AnalyzerKeysJobBean[]> getAnalyzerKeysJobBean(@Query("title") String title,
                                                             @Query("size") int size);
    @POST("user/captcha")
    Observable<JsonCaptchaBean> getCaptcha(@Query("tel") String tel);

    @POST("user/login")
    Observable<JsonLoginSuccessBean> getUserId(@Query("tel") String tel,
                                               @Query("captcha") int captcha);

    @POST("user/uid")
    Observable<JsonUserInformationBean> getUserinfo(@Query("uid") String userId);



}
