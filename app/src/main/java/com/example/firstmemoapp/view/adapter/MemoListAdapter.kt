package com.example.firstmemoapp.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.firstmemoapp.R
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.viewModel.MemoListViewModel
import com.example.firstmemoapp.viewModel.WordViewModel
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MemoListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<MemoListAdapter.MemoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var memoDataList = emptyList<Memo>() // Cached copy of words

    internal fun setMemoList(memoDataList: List<Memo>) {
        this.memoDataList = memoDataList
        notifyDataSetChanged()
    }

    private var wordViewModel: MemoListViewModel =
        ViewModelProvider(context as ViewModelStoreOwner).get(MemoListViewModel::class.java)

    inner class MemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //todo val memoListItemView: Viewを作っていれる
        val testTextView: TextView = itemView.findViewById(R.id.test_title_test)
        val testDateTextView: TextView = itemView.test_date_test
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        //todo こっちのviewの参照先も書き換える
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return MemoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        Log.i("kinoshita", "onBindViewHolder: position = " + position)
        holder.testTextView.text = memoDataList[position].title
        holder.testDateTextView.text = memoDataList[position].date
        holder.itemView.setOnClickListener {
            //todo: 編集フラグメントへの遷移　20210109
        }
    }

    override fun getItemCount() = memoDataList.size
}