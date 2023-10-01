package com.hara.kaera.domain.entity.login

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginJWTEntity(
    val accessToken: String,
    val refreshToken: String,
    val name: String,
)