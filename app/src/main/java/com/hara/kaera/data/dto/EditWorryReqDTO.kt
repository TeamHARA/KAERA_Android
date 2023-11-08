package com.hara.kaera.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditWorryReqDTO(
    @SerialName("worryId")
    val worryId: Int,
    @SerialName("templateId")
    val templateId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("answers")
    val answers: List<String>
)