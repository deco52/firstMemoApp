package com.example.firstmemoapp.view.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.example.firstmemoapp.databinding.FragmentEditTaskBinding
import com.example.firstmemoapp.google.EventObserver
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.service.model.TaskStatus
import com.example.firstmemoapp.view.ui.dialog.DatePick
import com.example.firstmemoapp.view.ui.dialog.TimePick
import com.example.firstmemoapp.viewModel.EditTaskViewModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

import android.widget.RadioButton

import android.widget.RadioGroup




class EditTaskFragment : Fragment(), DatePick.DatePickerDialogListener,
    TimePick.TimePickerDialogListener {
    private lateinit var binding: FragmentEditTaskBinding
    private val viewModel: EditTaskViewModel by activityViewModels()

    private lateinit var task: Task

    private var date: LocalDate? = null
    private var time: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Taskオブジェクトの初期化
        // 設定した requestKey を元にbundleを受け取る
        setFragmentResultListener("request_key") { requestKey, bundle ->
            task = bundle.getSerializable("select_task") as Task
            viewModel.setTask(task)
            viewModel.title.value = task.title
            viewModel.text.value = task.text

            Log.i(
                "kinoshita",
                "setTask  task_id:" + task.task_id + "task_title" + task.title
            )

            // 期限を超えているかチェック
            val isOverPeriodTime = task.period_time.before(Timestamp(System.currentTimeMillis()))

            // 時間を参照して適切なステータスを設定
            var tmpTaskStatus = task.status
            if (isOverPeriodTime && tmpTaskStatus == TaskStatus.NO_START.id){
                tmpTaskStatus = TaskStatus.PERIOD_OVER.id
            }
            when(tmpTaskStatus) {
                TaskStatus.DONE.id -> binding.doneRadio.isChecked = true
                TaskStatus.NO_START.id -> binding.noStartRadio.isChecked = true
                TaskStatus.PERIOD_OVER.id -> binding.noStartRadio.isChecked = true
            }

            binding.periodDate.text = SimpleDateFormat("yyyy年M月d日(E)").format(task.period_time)
            binding.periodTime.text = SimpleDateFormat("HH:mm").format(task.period_time)
        }

        //
        binding.statusRadios.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selectedgro
            val id =group.checkedRadioButtonId
            val radioButton = group.findViewById<RadioButton>(id)
            val index = group.indexOfChild(radioButton)

            // 期限を超えているかチェック
            val isOverPeriodTime = task.period_time.before(Timestamp(System.currentTimeMillis()))

            var updateStatus = 0

            when (index) {
                0 -> updateStatus = if(isOverPeriodTime) TaskStatus.PERIOD_OVER.id else TaskStatus.NO_START.id
                1 -> updateStatus = TaskStatus.DONE.id
//                else -> updateStatus = TaskStatus.NO_START.id
            }

            var updateTask = Task(
                task.task_id,
                task.title,
                task.text,
                updateStatus,
                task.status,
                task.type,
                task.notification,
                task.period_time,
                task.register_time,
                task.update_time
            )
            task = updateTask
            viewModel.setTask(task)
        })

        // 期限設定変更リスナー
        binding.periodDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.periodTime.setOnClickListener {
            showTimePickerDialog()
        }


        // 画面遷移用のイベントを監視 ViewModelから通知が来たらリスト戻る
        viewModel.onTransit.observe(viewLifecycleOwner, EventObserver {
            parentFragmentManager.popBackStack()
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePickerDialog() {
        var fragment: DatePick? = null
        if (date != null) {
            fragment = DatePick.newInstance(date!!.year, date!!.monthValue, date!!.dayOfMonth)
        }
        if (fragment == null) {
            fragment = DatePick.newInstance()
        }
        fragment.show(childFragmentManager, "DatePickerDialogFragment")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showTimePickerDialog() {
        var fragment: TimePick? = null
        if (time != null) {
            fragment = TimePick.newInstance(time!!.hour, time!!.minute)
        }
        if (fragment == null) {
            fragment = TimePick.newInstance()
        }
        fragment.show(childFragmentManager, "TimePickerDialogFragment")
    }

    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        var str: String = String.format(Locale.US, "%d-%d-%d ", year, month, dayOfMonth)
        Log.i(
            "kinoshita",
            "onDateSet: ${str + SimpleDateFormat("HH:mm:00").format(task.period_time.time)}"
        )

        val updateTimeStamp =
            Timestamp.valueOf(str + SimpleDateFormat("HH:mm:00").format(task.period_time.time))
        var updateTask = Task(
            task.task_id,
            task.title,
            task.text,
            task.status,
            task.last_status,
            task.type,
            task.notification,
            updateTimeStamp,
            task.register_time,
            task.update_time
        )
        task = updateTask
        viewModel.setTask(task)

        binding.periodDate.text = SimpleDateFormat("yyyy年M月d日(E)").format(task.period_time)
    }

    override fun onTimeSet(hourOfDay: Int, minute: Int) {
        var str: String = String.format(Locale.US, " %d:%d:00", hourOfDay, minute)
        Log.i(
            "kinoshita",
            "onTimeSet: ${SimpleDateFormat("yyyy-M-dd").format(task.period_time) + str}"
        )

        val updateTimeStamp =
            Timestamp.valueOf(SimpleDateFormat("yyyy-M-dd").format(task.period_time) + str)
        var updateTask = Task(
            task.task_id,
            task.title,
            task.text,
            task.status,
            task.last_status,
            task.type,
            task.notification,
            updateTimeStamp,
            task.register_time,
            task.update_time
        )
        task = updateTask
        viewModel.setTask(task)

        binding.periodTime.text = SimpleDateFormat("HH:mm").format(task.period_time)
    }
}