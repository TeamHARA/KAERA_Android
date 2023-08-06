package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailDTO(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    @Serializable
    data class Data(
        val guideline: String,
        val hints: List<String>,
        val questions: List<String>,
        val title: String
    )
}