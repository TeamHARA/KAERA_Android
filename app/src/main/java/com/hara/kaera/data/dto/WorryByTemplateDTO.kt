package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorryByTemplateDTO(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean,
) {
    @Serializable
    data class Data(
        val totalNum: Int,
        val worry: List<Worry>,
    ) {
        @Serializable
        data class Worry(
            val period: String,
            val templateId: Int,
            val title: String,
            val worryId: Int,
        )
    }
}
