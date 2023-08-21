package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class WorryByTemplateEntity(
    val errorMessage: String?,
    val worryByTemplate: WorryByTemplate?,
) {
    @Serializable
    data class WorryByTemplate(
        val totalNum: Int,
        val worryList: List<Worry>,
    ) {
        @Serializable
        data class Worry(
            val period: String,
            val templateId: Int,
            val title: String,
            val worryId: Int,
        )
    }
}
