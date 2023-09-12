package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorryDetailDTO(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean,
) {
    @Serializable
    data class Data(
        val answers: List<String>,
        val `d-day`: Int,
        val deadline: String,
        val finalAnswer: String,
        val period: String,
        val review: Review,
        val subtitles: List<String>,
        val templateId: Int,
        val title: String,
        val updatedAt: String,
    ) {
        @Serializable
        data class Review(
            val content: String,
            val updatedAt: String,
        )
    }
}