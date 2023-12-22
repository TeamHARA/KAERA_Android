package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorryDetailDTO(
    val status: Int,
    val success: Boolean,
    val message: String,
    val `data`: Data,
) {
    @Serializable
    data class Data(
        val title: String,
        val templateId: Int,
        val subtitles: List<String>,
        val answers: List<String>,
        val period: String,
        val updatedAt: String,
        val deadline: String,
        val dDay: Int,
        val finalAnswer: String?, // 고민 해결 전이면 ""
        val review: Review, // 고민 해결 전이면 서버에서 null로 온다
    ) {
        @Serializable
        data class Review(
            val content: String?, // 고민 해결 전이면 ""
            val updatedAt: String?, // 고민 해결 전이면 ""
        )
    }
}