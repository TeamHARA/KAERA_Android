package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class DeleteWorryEntity(
    val message: String,
    val status: Int,
    val success: Boolean,
)
