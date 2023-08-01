package com.hara.kaera.presentation.storage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageViewModel : ViewModel() {
    private val _templateId = MutableLiveData<Int>(-1)
    val templateId get() = _templateId

    fun setTemplateId(choiceId: Int) {
        _templateId.value = choiceId
    }
}
