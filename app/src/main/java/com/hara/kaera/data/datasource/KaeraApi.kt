package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.TemplateTypeDTO
import retrofit2.http.GET

/*
실제 api들을 적어두는 인터페이스 그대로 쓰면된다!
 */

interface KaeraApi {
    @GET("template")
    suspend fun getTemplateTypesInfo(): TemplateTypeDTO

}