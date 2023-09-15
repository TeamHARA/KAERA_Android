package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewResDTO(
    val `data`: Data,
    val message: String, // 나의 기록 등록 및 수정 성공
    val status: Int, // 200
    val success: Boolean, // true
) {
    @Serializable
    data class Data(
        val updatedAt: String, // 2019-10-01
    )
}
