package com.hara.kaera.data.datasource

import com.hara.kaera.data.data.TemplateTypeDTO
import retrofit2.http.GET

interface KaeraService {

    @GET("/template")
    suspend fun getTemplateTypesInfo(): TemplateTypeDTO

}