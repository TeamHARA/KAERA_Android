package com.hara.kaera.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditDeadlineReqDTO(
    @SerialName("worryId")
    val worryId: Int,
    @SerialName("dayCount")
    val dayCount: Int
)