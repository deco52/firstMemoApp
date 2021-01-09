package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.FragmentTopMemoListBinding
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.view.adapter.MemoListAdapter
import com.example.firstmemoapp.viewModel.MemoListViewModel
import kotlinx.android.synthetic.main.fragment_top_memo_list.*

class TopMemoListFragment : Fragment() {

    private lateinit var viewModel: MemoListViewModel
    private lateinit var binding: FragmentTopMemoListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTopMemoListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_top_memo_list, container, false)
        this.binding = binding
        return binding.root
        //todo 2020/12/12 RecyclerViewの枠表示までできた
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //todo observeでrepositoryからリストを取得する形に修正する事
        var adapter = MemoListAdapter(requireContext())
        var memo: Memo = Memo(0,"testTitle", "testText", "2021-01-09")
        var memo2: Memo = Memo(0,"testTitle2", "testText", "2021-01-09")
        var list: List<Memo> = listOf(memo, memo2)
        adapter.setMemoList(list)

        memo_recycler_view.adapter = adapter//MemoListAdapter(requireContext())
        memo_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }
}