package com.hara.kaera.data.dto.login

data class JWTRefreshReqDTO(
    val accessToken: String, // 만료된 jwt 액세스토큰
    val refreshToekn: String // jwt 리프레쉬토큰
)