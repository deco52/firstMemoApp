package com.example.roomwordsample.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NewWordViewModel {
    private val _buttonText =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "default"
        }
    val buttonText: LiveData<String>
        get() = _buttonText

    private val _isEnabled: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().also { mutableLiveData ->
            mutableLiveData.value = false
        }

    val isEnabled: LiveData<Boolean>
        get() = _isEnabled

    fun updateButtonText(isBlank: Boolean) {
        _isEnabled.value = !isBlank

        if (!isBlank) {
            _buttonText.value = "Save"
        } else {
            _buttonText.value = "Ready…"
        }
    }
}