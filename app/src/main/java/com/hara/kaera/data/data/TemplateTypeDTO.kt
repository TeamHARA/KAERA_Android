package com.hara.kaera.data.data

import kotlinx.serialization.Serializable

@Serializable
data class TemplateTypeDTO(
    val `data`: List<Data>,
    val message: String, // 모든 템플릿 조회 성공
    val status: Int, // 200
    val success: Boolean // true
) {
    @Serializable
    data class Data(
        val hasUsed: Boolean, // false
        val info: String, // 어떤 질문도 던지지 않아요. 캐라 도화지에서 머릿 속 얽혀있는 고민 실타래들을 마음껏 풀어내세요!
        val shortInfo: String, // 빈 공간을 자유롭게 채우기
        val templateId: Int, // 1
        val title: String // Free Flow
    )
}
