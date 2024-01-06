package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushAlarmReqDTO(
    @SerialName("deviceToken")
    val deviceToken: String
)