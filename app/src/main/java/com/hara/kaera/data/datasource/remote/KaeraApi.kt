package com.hara.kaera.data.datasource.remote

import com.hara.kaera.data.dto.DecideFinalReqDTO
import com.hara.kaera.data.dto.DecideFinalResDTO
import com.hara.kaera.data.dto.DeleteWorryDTO
import com.hara.kaera.data.dto.EditDeadlineReqDTO
import com.hara.kaera.data.dto.EditDeadlineResDTO
import com.hara.kaera.data.dto.EditWorryReqDTO
import com.hara.kaera.data.dto.EditWorryResDTO
import com.hara.kaera.data.dto.HomeWorryListDTO
import com.hara.kaera.domain.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.login.KaKaoLoginResDTO
import com.hara.kaera.data.dto.ReviewReqDTO
import com.hara.kaera.data.dto.ReviewResDTO
import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import com.hara.kaera.data.dto.WorryByTemplateDTO
import com.hara.kaera.data.dto.WorryDetailDTO
import com.hara.kaera.data.dto.WriteWorryReqDTO
import com.hara.kaera.data.dto.WriteWorryResDTO
import com.hara.kaera.data.dto.login.ServiceDisConnectResDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/*
실제 api들을 적어두는 인터페이스 그대로 쓰면된다!
 */

interface KaeraApi {

    @GET("template")
    suspend fun getTemplateTypesInfo(): TemplateTypeDTO

    @GET("template/{templateId}")
    suspend fun getTemplateDetail(
        @Path("templateId") templateId: Int,
    ): TemplateDetailDTO

    @GET("worry/list/{isSolved}")
    suspend fun getHomeWorryList(
        @Path("isSolved") isSolved: Int,
    ): HomeWorryListDTO

    @GET("worry/")
    suspend fun getWorryByTemplate(
        @Query("templateId") templateId: Int,
    ): WorryByTemplateDTO

    @POST("user/kakao/login")
    suspend fun getKakaoLoginJTW(
        @Body kaKaoLoginReqDTO: KaKaoLoginReqDTO
    ): KaKaoLoginResDTO

    @GET("worry/{worryId}")
    suspend fun getWorryDetail(
        @Path("worryId") worryId: Int,
    ): WorryDetailDTO

    @DELETE("worry/{worryId}")
    suspend fun deleteWorry(
        @Path("worryId") worryId: Int,
    ): DeleteWorryDTO

    @PATCH("review")
    suspend fun updateReview(
        @Body reviewReqDto: ReviewReqDTO,
    ): ReviewResDTO

    @PATCH("worry")
    suspend fun editWorry(
        @Body editWorryReqDTO: EditWorryReqDTO
    ): EditWorryResDTO

    @PATCH("worry/deadline")
    suspend fun editDeadline(
        @Body editDeadlineReqDTO: EditDeadlineReqDTO
    ): EditDeadlineResDTO

    @POST("worry")
    suspend fun writeWorry(
        @Body writeWorryReqDTO: WriteWorryReqDTO
    ): WriteWorryResDTO

    @PATCH("worry/finalAnswer")
    suspend fun decideFinal(
        @Body decideFinalReqDTO: DecideFinalReqDTO
    ): DecideFinalResDTO

    @POST("auth/logout")
    suspend fun serviceLogout(): ServiceDisConnectResDTO

    @POST("auth/unregister")
    suspend fun serviceUnRegister(): ServiceDisConnectResDTO

}
