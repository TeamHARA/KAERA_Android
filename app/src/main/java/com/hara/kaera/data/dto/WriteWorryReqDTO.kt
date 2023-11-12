package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteWorryReqDTO(
    @SerialName("templateId")
    val templateId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("answers")
    val answers: List<String>,
    @SerialName("deadline")
    val deadline: Int
)