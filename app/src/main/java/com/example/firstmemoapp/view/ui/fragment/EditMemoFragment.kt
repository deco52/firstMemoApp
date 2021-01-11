package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.firstmemoapp.databinding.FragmentEditMemoBinding
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.viewModel.EditMemoViewModel

class EditMemoFragment : Fragment() {
    private lateinit var binding: FragmentEditMemoBinding
    private val viewModel = EditMemoViewModel()

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定した requestKey を元にbundleを受け取る
        setFragmentResultListener("request_key") { requestKey, bundle ->
            val memo: Memo = bundle.getSerializable("select_memo") as Memo
            binding.memoTitle.setText(memo.title)
            binding.memoText.setText(memo.text)
        }
    }
}