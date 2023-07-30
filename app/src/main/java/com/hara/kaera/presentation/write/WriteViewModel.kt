package com.hara.kaera.presentation.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WriteViewModel : ViewModel() {
    private val _templateId = MutableLiveData<Int>(-1)
    val templateId get() = _templateId

    fun setTemplateId(choiceId: Int) {
        _templateId.value = choiceId
    }

}