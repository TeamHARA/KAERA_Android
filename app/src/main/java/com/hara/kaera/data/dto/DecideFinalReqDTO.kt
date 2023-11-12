package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DecideFinalReqDTO (
    @SerialName("worryId")
    val worryId: Int,
    @SerialName("finalAnswer")
    val finalAnswer: String,
)