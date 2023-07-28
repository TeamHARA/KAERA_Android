package com.hara.kaera.data.datasource

import com.hara.kaera.data.dto.TemplateTypeDTO
import kotlinx.coroutines.flow.Flow

/*
    실제로 데이터를 받아오는곳 여기서 status코드로 현재 서버상태를 판별하는게 정석적이지만
    그것 나중에 하도록 하고 정석대로라면 remote / local의 datasoure가 각각 있을것 하지만
    우리는 local은 없으므로 remote만 있는 상태
 */

interface KaeraDataSource {

    fun getTemplateTypesInfo(): Flow<TemplateTypeDTO>

}