package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteWorryResDTO(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("worryId")
        val worryId: Int,
        @SerialName("title")
        val title: String,
        @SerialName("templateId")
        val templateId: Int,
        @SerialName("subtitles")
        val subtitles: List<String>,
        @SerialName("answers")
        val answers: List<String>,
        @SerialName("createdAt")
        val createdAt: String,
        @SerialName("deadline")
        val deadline: String,
        @SerialName("dDay")
        val dDay: Int,
    )
}