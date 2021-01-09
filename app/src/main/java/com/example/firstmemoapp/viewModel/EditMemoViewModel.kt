package com.example.firstmemoapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EditMemoViewModel {
    private val _titleHint =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "Title…"
        }
    val titleHint: LiveData<String>
        get() = _titleHint

    private val _textHint =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "Text…"
        }
    val textHint: LiveData<String>
        get() = _textHint

    fun updateTitle(isBlank: Boolean) {
        if (!isBlank) {
            _titleHint.value
        } else {
            _titleHint.value = "Title…"
        }
    }

    fun updateText(isBlank: Boolean) {
        if (!isBlank) {
            _titleHint.value = ""
        } else {
            _titleHint.value = "Text…"
        }
    }

}