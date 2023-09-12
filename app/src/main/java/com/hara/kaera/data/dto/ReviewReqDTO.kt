package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewReqDTO(
    val worryId: Int,
    val review: String,
)
