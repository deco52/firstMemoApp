package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.FragmentEditMemoBinding
import com.example.firstmemoapp.google.EventObserver
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.viewModel.EditMemoViewModel

class EditMemoFragment : Fragment() {
    private lateinit var binding: FragmentEditMemoBinding
    private val viewModel: EditMemoViewModel by activityViewModels()

    private lateinit var memo: Memo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMemoBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定した requestKey を元にbundleを受け取る
        setFragmentResultListener("request_key") { requestKey, bundle ->
            memo = bundle.getSerializable("select_memo") as Memo
            viewModel.setMemo(memo)
            viewModel.title.value = memo.title
            viewModel.text.value = memo.text
        }

        // 画面遷移用のイベントを監視 ViewModelから通知が来たらリスト戻る
        viewModel.onTransit.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.popBackStack()
        })
    }
}