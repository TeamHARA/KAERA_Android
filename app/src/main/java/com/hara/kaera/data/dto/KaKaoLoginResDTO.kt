package com.hara.kaera.data.dto

data class KaKaoLoginResDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String, // 로그인 성공
    val `data`: Data
) {
    data class Data(
        val id: Int, // 2
        val name: String, // 김캐라
        val accessToken: String // 캐라의 jwt 액세스토큰
    )
}