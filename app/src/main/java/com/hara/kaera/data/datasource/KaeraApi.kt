package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/*
실제 api들을 적어두는 인터페이스 그대로 쓰면된다!
 */

interface KaeraApi {

    @GET("template")
    suspend fun getTemplateTypesInfo(): TemplateTypeDTO

    @GET("template/{templateId}")
    suspend fun getTemplateDetail(
        @Path("templateId") templateId: Int
    ): TemplateDetailDTO

}