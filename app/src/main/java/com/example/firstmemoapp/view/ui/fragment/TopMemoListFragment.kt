package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.FragmentTopMemoListBinding
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.view.adapter.MemoListAdapter
import com.example.firstmemoapp.viewModel.MemoListViewModel
import kotlinx.android.synthetic.main.fragment_top_memo_list.*

class TopMemoListFragment : Fragment() {

    private val viewModel: MemoListViewModel by activityViewModels()
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
        var memo: Memo = Memo(0, "testTitle", "testText", "2021-01-09")
        var memo2: Memo = Memo(0, "testTitle2", "testText", "2021-01-09")
        var list: List<Memo> = listOf(memo, memo2)

        //todo 1: 起動時にデータを追加する処理（とりあえず入れる）
        var tmpMemo = Memo(0, "test1", "test1", "20211030")
        var tmpList: ArrayList<Memo> = ArrayList<Memo>(list)
        // 普通に呼べば更新できる
        tmpList.add(tmpMemo)

        this.viewModel.insert(tmpMemo)
        this.viewModel.showAll()
        if (this.viewModel.allMemos.value != null) {
            for (value in ArrayList<Memo>()) {
                tmpList.add(value)
            }
        }

        //todo 2:データを参照してリストに表示する処理

        adapter.setOnMemoListClickListener(
            object : MemoListAdapter.OnMemoListClickListener {
                override fun onItemClick(memo: Memo) {
//                    viewModel.deleteAll()

                    //遷移処理
                    val fragmentManager: FragmentManager? = parentFragmentManager
                    if(fragmentManager != null) {
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                        )

                        setFragmentResult("request_key", bundleOf(
                            "select_memo" to memo
                        ))

                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.replace(R.id.container, EditMemoFragment())
                        fragmentTransaction.commit()
                    }
                }
            }
        )
//        adapter.setMemoList(viewModel.allMemos.value
        // データベースを監視 ＞　中身を取得してリストに表示
        this.viewModel.memoDao.getAllMemos().observe(viewLifecycleOwner, Observer {
            adapter.setMemoList(it)
            adapter.notifyDataSetChanged()
        })

        memo_recycler_view.adapter = adapter//MemoListAdapter(requireContext())
        memo_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }
}