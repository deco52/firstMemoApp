package com.example.firstmemoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.firstmemoapp.google.Event
import com.example.firstmemoapp.service.dao.MemoDao
import com.example.firstmemoapp.service.database.MemoRoomDatabase
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.service.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditMemoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MemoRepository
    var memoDao: MemoDao = MemoRoomDatabase.getDatabase(application, viewModelScope).memoDao()
    private lateinit var memo: Memo

    // 画面遷移用のイベント定義
    val onTransit = MutableLiveData<Event<String>>()

    init {
        repository = MemoRepository(memoDao)
    }

    // EditTextの値
    var title: MutableLiveData<String> = MutableLiveData<String>()
    var text: MutableLiveData<String> = MutableLiveData<String>()

    // LiveDataの残骸
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


    // TODO: 20211107 今日はここまで　次回　新規と削除やる
    //        PS:EditTExt開くとレイアウト崩れのバグあり
    fun onClickUpdateButton() {
        Log.i("kinoshita", "Update:${memo.id}")

        var memo = Memo(memo.id, title.value ?: "", text.value ?: "", "YYYYMMDD")

        viewModelScope.launch(Dispatchers.Main) {
            repository.update(memo)
        }
    }

    fun onClickInsertButton() {
        Log.i("kinoshita", "Insert:${memo.id}")

        var memo = Memo(0, title.value ?: "", text.value ?: "", "YYYYMMDD")
        viewModelScope.launch(Dispatchers.Main) {
            repository.insert(memo)
        }
        // 成功：リストへ遷移
        onTransit.value = Event("onTransit")
    }

    fun onClickDeleteButton() {
        Log.i("kinoshita", "Delete:${memo.id}")

        // 削除確認ダイアログ

        // 削除処理
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteMemo(memo.id)
        }
        // 成功：リストへ遷移
        onTransit.value = Event("onTransit")
    }

    fun setMemo(memo: Memo) {
        this.memo = memo
        Log.i("kinoshita", memo.id.toString())
    }

}