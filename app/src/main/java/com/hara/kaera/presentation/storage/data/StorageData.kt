package com.hara.kaera.presentation.storage.data

data class StorageData(
    val totalNum: Int,
    val worryList: List<Worry>,
)

data class Worry(
    val worryId: Int,
    val title: String,
    val period: String,
    val templateId: Int
)
