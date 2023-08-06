package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailEntity (
    val errorMessage :String?,
    val templateDetailInfo: TemplateDetailInfo?
) {
    @Serializable
    data class TemplateDetailInfo(
        val guideline: String,
        val hints: List<String>,
        val questions: List<String>,
        val title: String
    )
}