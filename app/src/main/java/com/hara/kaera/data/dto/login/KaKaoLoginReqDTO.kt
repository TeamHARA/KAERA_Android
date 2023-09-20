package com.hara.kaera.data.dto.login

import kotlinx.serialization.Serializable

@Serializable
data class KaKaoLoginReqDTO(
    val accessToken: String // 카카오 액세스토큰
)