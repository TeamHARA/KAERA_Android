package com.hara.kaera.presentation.write.data

/*
서버 명세서 기반!
 */
data class TemplateData(
    val templateId: Int,
    val title: String,
    val info: String,
    val hasUsed: Boolean,
)
