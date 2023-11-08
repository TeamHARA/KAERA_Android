package com.hara.kaera.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLogoutResDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String // 로그아웃 성공
)