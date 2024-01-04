package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PushAlarmResDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String // 알림 비활성화 성공
)