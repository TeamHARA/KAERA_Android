package com.hara.kaera.domain.entity.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val name: String? = null,
    val userId: Int? = null,
)