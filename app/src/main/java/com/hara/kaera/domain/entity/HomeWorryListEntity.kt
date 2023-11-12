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
    ) {
        override fun hashCode(): Int {
            return worryId
        }

        override fun equals(other: Any?): Boolean {
            val casted = other as HomeWorry
            return this.worryId == casted.worryId
        }
    }
}