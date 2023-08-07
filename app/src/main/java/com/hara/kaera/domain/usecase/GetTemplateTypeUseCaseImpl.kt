package com.hara.kaera.domain.usecase

import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.repository.KaeraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
뷰모델에서 실제로 서버통신을 위해 호출할 UseCase
뷰모델에서 전체 레포짓토리를 알기보다는 진짜 필요한 서버통신만들 하기 위해서
다음과 같이 레포짓토리를 주입받고 특정 레포짓토리 함수만 호출한다! 이때 repository에서 무언가 가공하는 과정이
없으므로 emit하지 않고 그대로 flow를 리턴한다.
 */

class GetTemplateTypeUseCaseImpl @Inject constructor(private val repository: KaeraRepository) :
    GetTemplateTypeUseCase {
    override fun getTemplateFlow(): Flow<TemplateTypesEntity> {
        return repository.getAllTemplateTypesInfo()
    }
}