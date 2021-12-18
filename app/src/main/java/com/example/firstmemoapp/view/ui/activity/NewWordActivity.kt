package com.example.firstmemoapp.view.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.ActivityNewWordBinding
import com.example.firstmemoapp.viewModel.NewWordViewModel

class NewWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewWordBinding

    private val viewModel = NewWordViewModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_word)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    public override fun onStart() {
        super.onStart()

        binding.editWord.addTextChangedListener { text ->
            Log.i("kinoshita", "textChangeListener: " + text)
            viewModel.updateButtonText(text.isNullOrBlank())
        }

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(binding.editWord.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val word = binding.editWord.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}