package com.example.firstmemoapp.view.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstmemoapp.R
import com.example.firstmemoapp.databinding.FragmentTopTaskListBinding
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.view.adapter.TaskListAdapter
import com.example.firstmemoapp.viewModel.TaskListViewModel
import kotlinx.android.synthetic.main.fragment_top_task_list.*


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

        //todo observeでrepositoryからリストを取得する形に修正する事
        var adapter = TaskListAdapter(requireContext())
//        var task: Task = Task(0, "testTitle", "testText", "2021-01-09")
//        var task2: Task = Task(0, "testTitle2", "testText", "2021-01-09")
//        var list: List<Task> = listOf(task, task2)

        //todo 1: 起動時にデータを追加する処理（とりあえず入れる）
//        var tmpTask = Task(0, "test1", "test1", "20211030")
//        var tmpList: ArrayList<Task> = ArrayList<Task>(list)
//        // 普通に呼べば更新できる
//        tmpList.add(tmpTask)

//        this.viewModel.insert(tmpTask)
//        this.viewModel.showAll()
//        if (this.viewModel.allTasks.value != null) {
//            for (value in ArrayList<Task>()) {
//                tmpList.add(value)
//            }
//        }

        //todo 2:データを参照してリストに表示する処理

        adapter.setOnTaskListClickListener(
            object : TaskListAdapter.OnTaskListClickListener {
                override fun onItemClick(task: Task) {
//                    viewModel.deleteAll()

                    //遷移処理
                    val fragmentManager: FragmentManager? = parentFragmentManager
                    if (fragmentManager != null) {
                        val fragmentTransaction: FragmentTransaction =
                            fragmentManager.beginTransaction()
                        fragmentTransaction.setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                        )

                        setFragmentResult(
                            "request_key", bundleOf(
                                "select_task" to task
                            )
                        )

                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.replace(R.id.container, EditMemoFragment())
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
                        if (isChecked) 1 else 0,
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
            adapter.setTaskList(it)
            adapter.notifyDataSetChanged()
        })

        task_recycler_view.adapter = adapter//TaskListAdapter(requireContext())
        task_recycler_view.layoutManager = LinearLayoutManager(requireContext())

        //　リストの定義ここまで


        // TODO: 1126時点　　　　TaskMockＵＩの作り込み > タスクDBの動作確認をする事
        // -1.mock fragmentへの画面遷移　眠い
        // TODO:1212 Dateの扱いに苦戦　Date を DATETIMEに変えれば解決しそうなので、次はこれを試す
        // -0.5 　データの作成(Task)まではできている　時間がないので↑で成功するか試す

        // TimeStampにする事で突破　20211218
        // selectしたらなぜか入ってない？ -> 入ってはいるみたい　一覧取得がうまくいかない


        // TODO：1218時点:いっそもうリストで出しちゃう　TaskListViewFragment(トップページ)をつくる
        // New / Edit の viewも作る　　ひとまず合計3つ
        // 1.リスト押下で洗濯したのをエディットに出す(select)
        // 2.日付指定もできるようにする
        // 3.登録日、更新日も入れる
        //、insert(新規ボタン)、update()

        // mockへの遷移
//        binding.mockButton.setOnClickListener {
//            //遷移処理
//            val fragmentManager: FragmentManager? = parentFragmentManager
//            if (fragmentManager != null) {
//                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.setCustomAnimations(
//                    android.R.anim.slide_in_left,
//                    android.R.anim.slide_out_right
//                )
//
//                fragmentTransaction.addToBackStack(null)
//                fragmentTransaction.replace(R.id.container, TaskMockFragment())
//                fragmentTransaction.commit()
//            }
//
//        }
    }
}