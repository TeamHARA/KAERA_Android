package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.DeleteWorryDTO
import com.hara.kaera.data.dto.HomeWorryListDTO
import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import com.hara.kaera.data.dto.WorryByTemplateDTO
import com.hara.kaera.data.dto.WorryDetailDTO
import retrofit2.http.DELETE
import retrofit2.http.GET
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

    @GET("worry/{worryId}")
    suspend fun getWorryDetail(
        @Path("worryId") worryId: Int,
    ): WorryDetailDTO

    @DELETE("worry/{worryId}")
    suspend fun deleteWorry(
        @Path("worryId") worryId: Int,
    ): DeleteWorryDTO
}
