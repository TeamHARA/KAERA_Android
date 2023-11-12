package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DecideFinalResDTO(
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
        @SerialName("quote")
        val quote: String
    )
}