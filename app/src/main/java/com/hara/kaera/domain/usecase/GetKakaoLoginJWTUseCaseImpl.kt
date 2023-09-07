package com.hara.kaera.domain.usecase

import com.hara.kaera.core.ApiResult
import com.hara.kaera.data.dto.KaKaoLoginReqDTO
import com.hara.kaera.domain.entity.KakaoLoginJWTEntity
import com.hara.kaera.domain.repository.KaeraRepository
import com.hara.kaera.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetKakaoLoginJWTUseCaseImpl @Inject constructor(private val repository: LoginRepository) :
    GetKakaoLoginJWTUseCase {
    override fun invoke(accessToken: KaKaoLoginReqDTO): Flow<ApiResult<KakaoLoginJWTEntity>> {
        return repository.getKakaoLoginJTW(accessToken)
    }
}