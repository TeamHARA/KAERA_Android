package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class HomeWorryListEntity(
    val homeWorryList: MutableList<HomeWorry>
) {
    @Serializable
    data class HomeWorry(
        val worryId: Int,
        val templateId: Int,
        val title: String
    )
}