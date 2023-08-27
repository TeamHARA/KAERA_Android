package com.hara.kaera.data.mapper

import com.hara.kaera.data.dto.TemplateDetailDTO
import com.hara.kaera.data.dto.TemplateTypeDTO
import com.hara.kaera.data.dto.HomeWorryListDTO
import com.hara.kaera.data.dto.WorryByTemplateDTO
import com.hara.kaera.domain.entity.TemplateDetailEntity
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.domain.entity.HomeWorryListEntity
import com.hara.kaera.presentation.util.Constant
import com.hara.kaera.domain.entity.WorryByTemplateEntity

/*
Mapper는 다음과 같이 DTO타입을 Entity형태로 즉. 실제로 사용할 데이터만 담아서
가공하는 역할 다음과 같이 status에 따라서 Entity의 가공을 다르게 해주고 있다.
물론 이형태 보다는 datasource에서 status로 판단해주는게 더 좋지만 그건 조금 더 공부해온 다음해
적용하고 핵심은 데이터를 DTO그대로 쓰는게 아니고 Entity라는 형태로 가공해서 써준다는 것!
 */

object Mapper {
    fun mapperToTemplateType(dto: TemplateTypeDTO): TemplateTypesEntity {
        if (dto.status in 400..499) { // 에러이므로 아무것도 넣지 않고 erroMessage에만 담아준다.
            return TemplateTypesEntity(
                errorMessage = "서버 상태가 불안정합니다. 잠시후 다시 시도해주세요",
                templateTypeList = null,
            )
        } else if (dto.status in 500..599) {
            return TemplateTypesEntity(
                errorMessage = "네트워크상태가 불안정합니다.",
                templateTypeList = null,
            )
        } else {
            val templateTypeList = mutableListOf<TemplateTypesEntity.Template>()
            dto.data.toList().forEach {
                templateTypeList.add(
                    TemplateTypesEntity.Template(
                        hasUsed = it.hasUsed,
                        info = it.info,
                        shortInfo = it.shortInfo,
                        templateId = it.templateId,
                        title = it.title,
                    ),
                )
            }
            return TemplateTypesEntity(
                errorMessage = null,
                templateTypeList = templateTypeList,
            )
        }
    }

    fun mapperToTemplateDetail(dto: TemplateDetailDTO): TemplateDetailEntity {
        return dto.data.let {
            TemplateDetailEntity(
                title = it.title,
                info = it.info,
                guideline = it.guideline,
                questions = it.questions,
                hints = it.hints,
            )
        }
    }

    fun mapperToHomeWorryList(dto: HomeWorryListDTO): HomeWorryListEntity {
        val worryList = mutableListOf<HomeWorryListEntity.HomeWorry>()
        var idx = 0
        if (dto.status in 200..399) {
            dto.data.forEach {
                worryList.add(
                    HomeWorryListEntity.HomeWorry(
                        worryId = it.worryId,
                        templateId = it.templateId,
                        title = it.title,
                        homeIndex = Constant.homeGemsSequence[idx++]
                    )
                )
            }
        }
        return HomeWorryListEntity(worryList)
    }

    fun mapperToStorageWorry(dto: WorryByTemplateDTO): WorryByTemplateEntity {
        if (dto.status in 400..499) {
            return WorryByTemplateEntity(
                errorMessage = "서버 상태가 불안정합니다. 잠시후 다시 시도해주세요",
                worryByTemplate = null,
            )
        } else if (dto.status in 500..599) {
            return WorryByTemplateEntity(
                errorMessage = "네트워크상태가 불안정합니다.",
                worryByTemplate = null,
            )
        } else {
            var worryByTemplate: WorryByTemplateEntity.WorryByTemplate
            dto.data.let {
                worryByTemplate = WorryByTemplateEntity.WorryByTemplate(
                    totalNum = it.totalNum,
                    worryList = it.worry.map { worryDto ->
                        WorryByTemplateEntity.WorryByTemplate.Worry(
                            period = worryDto.period,
                            templateId = worryDto.templateId,
                            title = worryDto.title,
                            worryId = worryDto.worryId,
                        )
                    },
                )
            }
            return WorryByTemplateEntity(
                errorMessage = null,
                worryByTemplate = worryByTemplate,
            )
        }
    }
}
