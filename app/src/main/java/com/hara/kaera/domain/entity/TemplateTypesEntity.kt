package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

/*
우리가 실제로 뷰에서 사용할 DTO를 가공한 데이터타입
 */

@Serializable
data class TemplateTypesEntity(
    val errorMessage :String?,
    val templateTypeList: List<Template>?
) {
    @Serializable
    data class Template(
        val hasUsed: Boolean, // false
        val info: String, // 어떤 질문도 던지지 않아요. 캐라 도화지에서 머릿 속 얽혀있는 고민 실타래들을 마음껏 풀어내세요!
        val shortInfo: String, // 빈 공간을 자유롭게 채우기
        val templateId: Int, // 1
        val title: String // Free Flow
    )
}