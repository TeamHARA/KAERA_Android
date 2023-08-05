package com.hara.kaera.presentation.storage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StorageViewModel : ViewModel() {
    private val _templateId = MutableLiveData<Int>(0)
    private val _selectedId = MutableLiveData<Int>()

    val templateId get() = _templateId
    val selectedId get() = _selectedId

    fun setTemplateId() {
        _templateId.value = _selectedId.value
    }
    fun setSelectedId(choiceId: Int) {
        _selectedId.value = choiceId
    }

    fun getJewels() {
        // TODO: 서버 통신
    }
}
