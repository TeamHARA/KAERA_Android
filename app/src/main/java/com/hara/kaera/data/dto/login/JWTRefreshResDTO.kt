package com.hara.kaera.data.dto.login

data class JWTRefreshResDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String, // 토큰 재발급 성공
    val `data`: Data
) {
    data class Data(
        val accessToken: String // 재발급받은 jwt 액세스토큰
    )
}