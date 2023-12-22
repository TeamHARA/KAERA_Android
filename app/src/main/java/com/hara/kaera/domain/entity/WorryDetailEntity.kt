package com.hara.kaera.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorryDetailEntity(
    val title: String,
    val templateId: Int,
    val subtitles: List<String>,
    val answers: List<String>,
    val period: String,
    val updatedAt: String,
    val deadline: String,
    val dDay: Int,
    val finalAnswer: String?,
    val review: Review,
) : Parcelable {
    @Parcelize
    data class Review(
        val content: String?,
        val updatedAt: String?,
    ) : Parcelable
}
