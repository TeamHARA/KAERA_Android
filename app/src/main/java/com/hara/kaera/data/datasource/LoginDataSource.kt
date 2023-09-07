package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.data.dto.KaKaoLoginResDTO
import kotlinx.coroutines.flow.Flow

interface LoginDataSource {
    fun getKakaoLoginJWT(accessToken: KaKaoLoginReqDTO): Flow<KaKaoLoginResDTO>
}