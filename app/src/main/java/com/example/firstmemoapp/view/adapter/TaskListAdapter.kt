package com.example.firstmemoapp.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstmemoapp.R
import com.example.firstmemoapp.service.model.Task
import java.text.SimpleDateFormat

class TaskListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var taskDataList = emptyList<Task>() // Cached copy of words

    // リストアイテムのクリック -> 画面遷移で使用
    private lateinit var listener: OnTaskListClickListener
    private lateinit var changeListener: OnTaskListChangeListener

    interface OnTaskListClickListener {
        fun onItemClick(task: Task)
    }

    interface OnTaskListChangeListener {
        fun onItemChange(task: Task, isChecked: Boolean)
    }

    fun setOnTaskListClickListener(listener: OnTaskListClickListener) {
        this.listener = listener
    }

    fun setOnTaskListChangeListener(changeListener: OnTaskListChangeListener) {
        this.changeListener = changeListener
    }

    internal fun setTaskList(taskDataList: List<Task>) {
        this.taskDataList = taskDataList
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val detailTextView: TextView = itemView.findViewById(R.id.details)
        val periodDateTextView: TextView = itemView.findViewById(R.id.period_time)
        val statusCheckBox: CheckBox = itemView.findViewById(R.id.statusCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_recyclerview_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        Log.i("kinoshita", "onBindViewHolder: position = " + position)
        Log.i("kinoshita", "onBindViewHolder: taskId = " + taskDataList[position].task_id)

        holder.titleTextView.text = taskDataList[position].title
        holder.detailTextView.text = taskDataList[position].text
        holder.periodDateTextView.text =
            SimpleDateFormat("期限：yyyy/MM/dd HH:mm:ss").format(taskDataList[position].period_time)
        when (taskDataList[position].status) {
            // TODO:チェック状態をTaskクラスで定数で持ちたい
            1 -> holder.statusCheckBox.isChecked = true
            else -> holder.statusCheckBox.isChecked = false
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(taskDataList[position])
        }
        holder.statusCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            changeListener.onItemChange(taskDataList[position], isChecked)
//            var tmp = taskDataList[position]
//            var updateTask = Task(
//                tmp.task_id,
//                tmp.title,
//                tmp.text,
//                if (isChecked) 1 else 0,
//                tmp.last_status,
//                tmp.type,
//                tmp.notification,
//                tmp.period_time,
//                tmp.register_time,
//                tmp.update_time
//            )
        }
    }

    override fun getItemCount() = taskDataList.size
}