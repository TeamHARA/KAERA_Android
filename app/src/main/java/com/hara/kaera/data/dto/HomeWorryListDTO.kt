package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeWorryListDTO(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val `data`: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("worryId")
        val worryId: Int,
        @SerialName("templateId")
        val templateId: Int,
        @SerialName("title")
        val title: String
    )
}