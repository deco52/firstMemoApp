package com.example.firstmemoapp.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.firstmemoapp.R
import com.example.firstmemoapp.viewModel.WordViewModel
import com.example.firstmemoapp.service.model.Word

class WordListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Word>() // Cached copy of words

    /**
     *  TODO:よくない実装な気がする？ -> Modelの考え方に反している？
     *  要素のクリックでDB操作を行いたいので呼び出し
     *
     *  context(FragmentやAppCompatActivity)　は
     *  ViewModelの状態(スクロール位置など)をよしなに保持してくれるViewModelStoreOwnerに変換できる
     */
    private var wordViewModel: WordViewModel =
        ViewModelProvider(context as ViewModelStoreOwner).get(WordViewModel::class.java)

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.word_item_text)
        val deleteItemButton: Button = itemView.findViewById(R.id.delete_word_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        holder.wordItemView.text = current.word

        holder.deleteItemButton.setOnClickListener() {
            //TODO: データベース削除
            Log.i("kinoshita", "onBindViewHolder: delete button click position:{$position} id:{${current.id}}"
            )
            wordViewModel.deleteWord(current.id)
        }
    }

    internal fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size
}