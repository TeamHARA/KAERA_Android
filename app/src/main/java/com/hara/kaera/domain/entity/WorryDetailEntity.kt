package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class WorryDetailEntity(
    val answers: List<String>,
    val d_day: Int,
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
