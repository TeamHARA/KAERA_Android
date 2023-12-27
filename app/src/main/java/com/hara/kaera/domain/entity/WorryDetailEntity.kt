package com.hara.kaera.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorryDetailEntity(
    var worryId: Int,
    var title: String,
    var templateId: Int,
    var subtitles: List<String>,
    var answers: List<String>,
    var period: String,
    var updatedAt: String,
    var deadline: String,
    var dDay: Int,
    var finalAnswer: String?,
    var review: Review,
) : Parcelable {
    @Parcelize
    data class Review(
        var content: String?,
        var updatedAt: String?,
    ) : Parcelable
}
