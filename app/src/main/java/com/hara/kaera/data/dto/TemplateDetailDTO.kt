package com.hara.kaera.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailDTO(
    val status: Int, // 200
    val success: Boolean, // true
    val message: String, // 템플릿 조회 성공
    val `data`: Data
) {
    @Serializable
    data class Data(
        val title: String, // 세번의 왜?
        val info: String, // 문제의 근본적인 이유를 찾아주는 3why 기법이에요.왜?, 그래서 왜?, 그리고 왜? 숨어있는 원인을 캐내 문제를 해결할 수 있어요.
        val guideline: String, // 내 고민 속 물음의 꼬리를 통해 해결책을 함께 찾아가요!
        val questions: List<String>,
        val hints: List<String>
    )
}