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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.FragmentTopTaskListBinding
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.model.TaskStatus
import com.example.firstmemoapp.view.adapter.TaskListAdapter
import com.example.firstmemoapp.viewModel.TaskListViewModel
import kotlinx.android.synthetic.main.fragment_top_task_list.*
import kotlinx.coroutines.launch
import java.sql.Timestamp


class TopTaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by activityViewModels()
    private lateinit var binding: FragmentTopTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTopTaskListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_top_task_list, container, false)
        this.binding = binding
        return binding.root
        //todo 2020/12/12 RecyclerViewの枠表示までできた
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 新規追加ボタン押下
        binding.addButton.setOnClickListener {
            val fragmentManager: FragmentManager? = parentFragmentManager
            if (fragmentManager != null) {
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.setCustomAnimations(
//                    android.R.anim.slide_in_left,
//                    android.R.anim.slide_out_right
//                )

                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.replace(R.id.container, NewTaskFragment())
                fragmentTransaction.commit()
            }
        }

        // リストの諸々設定
        var adapter = TaskListAdapter(requireContext())
        adapter.setOnTaskListClickListener(
            object : TaskListAdapter.OnTaskListClickListener {
                override fun onItemClick(task: Task) {
                    //遷移処理
                    val fragmentManager: FragmentManager? = parentFragmentManager
                    if (fragmentManager != null) {
                        val fragmentTransaction: FragmentTransaction =
                            fragmentManager.beginTransaction()
//                        fragmentTransaction.setCustomAnimations(
//                            android.R.anim.slide_in_right,
//                            android.R.anim.slide_out_left
//                        )
                        setFragmentResult(
                            "request_key", bundleOf(
                                "select_task" to task
                            )
                        )
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.replace(R.id.container, EditTaskFragment())
                        fragmentTransaction.commit()
                    }
                }
            }
        )
        adapter.setOnTaskListChangeListener(
            object : TaskListAdapter.OnTaskListChangeListener {
                override fun onItemChange(task: Task, isChecked: Boolean) {
                    var tmp = task
                    var updateTask = Task(
                        tmp.task_id,
                        tmp.title,
                        tmp.text,
                        // 適切なステータス状態をセット
                        if (isChecked) TaskStatus.DONE.id
                        else {
                            if (tmp.period_time.before(Timestamp(System.currentTimeMillis()))){
                                TaskStatus.PERIOD_OVER.id
                            } else {
                                TaskStatus.NO_START.id
                            }
                        },
                        tmp.last_status,
                        tmp.type,
                        tmp.notification,
                        tmp.period_time,
                        tmp.register_time,
                        tmp.update_time
                    )

                    viewModel.update(updateTask)
                }
            }
        )
        // データベースを監視 ＞　中身を取得してリストに表示
        this.viewModel.taskDao.getAllTasks().observe(viewLifecycleOwner, Observer {
            // リスト取得時に登録タスクが0件ならば、サンプルを挿入
            if (it.isEmpty()) {
                var now = Timestamp(System.currentTimeMillis())
                var sampleTask = Task(
                    0, "てすとの", "サンプルだお！",
                    0, 0, 2, 0,
                    now, now, now
                )
                lifecycleScope.launch {
                    viewModel.taskDao.insert(sampleTask)
                }
            }

            // リストのソート方法を指定
            val periodComparator : Comparator<Task> = compareBy { it.period_time }
            val statusComparator : Comparator<Task> = compareBy { it.status }
            // リストに中身を表示

            adapter.setTaskList(it.sortedWith(periodComparator).sortedWith(statusComparator))

            var tmp: List<Task> = it.sortedWith(periodComparator).sortedWith(statusComparator)
            tmp.forEach {
                Log.i("kinoshita", "task status = " + it.status)
            }

            adapter.notifyDataSetChanged()
        })
        task_recycler_view.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager(context).getOrientation()))
        task_recycler_view.adapter = adapter//TaskListAdapter(requireContext())
        task_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    }
}