package com.hara.kaera.presentation.write.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WriteViewModel : ViewModel() {
    private val _templateId = MutableLiveData<Int>(-1)
    val templateId get() = _templateId

    fun setTemplateId(choiceId: Int) {
        _templateId.value = choiceId
    }

}