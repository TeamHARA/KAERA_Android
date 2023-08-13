package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailEntity (
    val errorMessage :String?,
    val templateDetailInfo: TemplateDetailInfo?
) {
    @Serializable
    data class TemplateDetailInfo(
        val title: String,
        val info: String,
        val guideline: String,
        val questions: List<String>,
        val hints: List<String>
    )
}