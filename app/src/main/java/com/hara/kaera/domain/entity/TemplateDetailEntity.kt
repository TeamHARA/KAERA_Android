package com.hara.kaera.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDetailEntity(
    val title: String,
    val guideline: String,
    val questions: List<String>,
    val hints: List<String>
)