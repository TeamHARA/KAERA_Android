package com.hara.kaera.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class KaKaoLoginResDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String, // 로그인 성공
    val `data`: Data
) {
    @Serializable
    data class Data(
        val id: Int, // 2
        val name: String, // 김캐라
        val accessToken: String, // 캐라의 jwt 액세스토큰
        val refreshToken: String, // 캐라의 jwt 리프레시토큰
    )
}