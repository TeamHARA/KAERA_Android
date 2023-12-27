package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class EditDeadlineEntity (
    val deadline: String,
    val dDay: Int
)