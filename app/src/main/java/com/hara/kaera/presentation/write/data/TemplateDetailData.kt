package com.hara.kaera.presentation.write.data

data class TemplateDetailData(
    val templateTitle: String,
    val templateInfo: String,
    val questions: List<String>,
    val hints: List<String>,
    val thanksTo: String?
)
