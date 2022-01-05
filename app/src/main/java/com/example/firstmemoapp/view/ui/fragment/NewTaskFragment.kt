package com.example.firstmemoapp.view.ui.fragment


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.example.firstmemoapp.databinding.FragmentNewTaskBinding
import com.example.firstmemoapp.google.EventObserver
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.viewModel.NewTaskViewModel
import com.example.firstmemoapp.view.ui.dialog.DatePick
import com.example.firstmemoapp.view.ui.dialog.TimePick
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


class NewTaskFragment : Fragment(), DatePick.DatePickerDialogListener,
    TimePick.TimePickerDialogListener {
    private lateinit var binding: FragmentNewTaskBinding
    private val viewModel: NewTaskViewModel by activityViewModels()

    private lateinit var task: Task

    private var date: LocalDate? = null
    private var time: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定した requestKey を元にbundleを受け取る
//        setFragmentResultListener("request_key") { requestKey, bundle ->
//            task = bundle.getSerializable("select_task") as Task
//            viewModel.setTask(task)
//            viewModel.title.value = task.title
//            viewModel.text.value = task.text
//        }

        // Taskオブジェクトの初期化
        var now = Timestamp(System.currentTimeMillis())
        task = Task(
            0, "", "",
            0, 0, 2, 0,
            now, now, now
        )
        viewModel.setTask(task)

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
        Log.i("kinoshita", "onDateSet: ${str + SimpleDateFormat("HH:mm:00").format(task.period_time.time)}")

        val updateTimeStamp = Timestamp.valueOf(str + SimpleDateFormat("HH:mm:00").format(task.period_time.time))
        var updateTask = Task(
            0,
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
        Log.i("kinoshita", "onTimeSet: ${SimpleDateFormat("yyyy-M-dd").format(task.period_time) + str}")

        val updateTimeStamp = Timestamp.valueOf(SimpleDateFormat("yyyy-M-dd").format(task.period_time) + str)
        var updateTask = Task(
            0,
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