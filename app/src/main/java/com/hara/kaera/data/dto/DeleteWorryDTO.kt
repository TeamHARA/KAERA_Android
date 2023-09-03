package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteWorryDTO(
    val message: String, // 고민 삭제 성공
    val status: Int, // 200
    val success: Boolean, // true
)
