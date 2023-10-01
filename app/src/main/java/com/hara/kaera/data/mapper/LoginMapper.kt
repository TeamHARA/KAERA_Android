package com.hara.kaera.data.mapper

import com.hara.kaera.data.dto.login.JWTRefreshResDTO
import com.hara.kaera.data.dto.login.KaKaoLoginResDTO
import com.hara.kaera.domain.entity.login.KakaoLoginJWTEntity

object LoginMapper {

    fun mapperToJWT(dto: KaKaoLoginResDTO): KakaoLoginJWTEntity {
        return KakaoLoginJWTEntity(
            accessToken = dto.data.accessToken,
            refreshToken = dto.data.refreshToken,
            name = dto.data.name,
        )
    }

    fun mapperToAccessToken(dto: JWTRefreshResDTO): String = dto.data.accessToken

}