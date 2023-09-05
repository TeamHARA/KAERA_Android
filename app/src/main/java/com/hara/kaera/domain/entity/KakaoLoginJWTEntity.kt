package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginJWTEntity(
    val accessToken: String,
    //val refreshToken: String,
)