package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import com.example.firstmemoapp.databinding.FragmentEditMemoBinding
import com.example.firstmemoapp.databinding.FragmentTaskMockBinding

import com.example.firstmemoapp.databinding.FragmentTaskMockBindingImpl

import com.example.firstmemoapp.google.EventObserver
import com.example.firstmemoapp.service.model.Memo
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.viewModel.EditMemoViewModel
import com.example.firstmemoapp.viewModel.TaskMockViewModel


/**
 * select, insert, update, delete の動きをテストします
 */
class TaskMockFragment : Fragment() {
    private lateinit var binding: FragmentTaskMockBinding
    private val viewModel: TaskMockViewModel by activityViewModels()

    private lateinit var memo: Memo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskMockBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定した requestKey を元にbundleを受け取る
//        setFragmentResultListener("request_key") { requestKey, bundle ->
//            memo = bundle.getSerializable("select_memo") as Memo
//            viewModel.setMemo(memo)
//            viewModel.title.value = memo.title
//            viewModel.text.value = memo.text

        // タスクに値をセット
        val tasks :LiveData<List<Task>> = this.viewModel.taskDao.getAllTasks()
        Log.i("kinoshita", "Size:${tasks.value?.size ?: 0}")

        Log.i("kinoshita", "Task:${tasks.value?.get(0)?.text}")


//            .observe(viewLifecycleOwner, Observer {
//            adapter.setMemoList(it)
//            adapter.notifyDataSetChanged()
//        })


        // 画面遷移用のイベントを監視 ViewModelから通知が来たらリスト戻る
        viewModel.onTransit.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.popBackStack()
        })
    }
}