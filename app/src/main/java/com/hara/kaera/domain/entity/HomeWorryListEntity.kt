package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class HomeWorryListEntity(
    val data: List<Data>?
) {
    @Serializable
    data class Data(
        val worryId: Int,
        val templateId: Int,
        val title: String,
        val homeIndex: Int
    )
}