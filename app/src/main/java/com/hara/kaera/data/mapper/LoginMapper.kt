package com.hara.kaera.data.mapper

import com.hara.kaera.data.dto.KaKaoLoginResDTO
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity

object LoginMapper {

    fun mapperToJWT(dto: KaKaoLoginResDTO): KakaoLoginJWTEntity {
        return KakaoLoginJWTEntity(
            accessToken = dto.data.accessToken,
            refreshToken = dto.data.refreshToken,
        )
    }
}