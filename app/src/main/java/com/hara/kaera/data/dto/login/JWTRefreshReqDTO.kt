package com.hara.kaera.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class JWTRefreshReqDTO(
    val accessToken: String, // 만료된 jwt 액세스토큰
    val refreshToken: String, // jwt 리프레쉬토큰
)