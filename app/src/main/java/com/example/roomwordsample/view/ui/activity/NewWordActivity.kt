package com.example.roomwordsample.view.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.roomwordsample.R
import com.example.roomwordsample.databinding.ActivityNewWordBinding

class NewWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewWordBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_word)

        binding.buttonText = "READY…"
        binding.editWord.addTextChangedListener() {
            when {
                binding.editWord.text.isEmpty() -> {
                    binding.buttonText = "READY…"
                    binding.buttonSave.isEnabled = false
                }
                else -> {
                    binding.buttonText = getString(R.string.button_save)
                    binding.buttonSave.isEnabled = true
                }
            }
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