package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

/*
DTO라는 이름은 애초에 변경이 가능한 클래스이다 그리도
아래를 보게되면 status, success등 뷰에서는 필요없는 정보들이 존재하므로
이런 필드들은 나중에 Mapper를 통해서 삭제될것이다. 하지만 서버에서 주는 데이터는 그대로 받아야 하므로
평소처럼 JsonToDataClass 플러그인으로 생성하면된다!
 */

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
