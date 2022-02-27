package com.example.firstmemoapp.view.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.firstmemoapp.R
import com.example.firstmemoapp.service.model.Task
import com.example.firstmemoapp.view.ui.activity.MainActivity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import kotlin.coroutines.coroutineContext
import com.google.android.material.snackbar.Snackbar

import android.R.layout
import com.example.firstmemoapp.service.model.TaskStatus


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
        // TODO:後日多言語対応したい
        holder.periodDateTextView.text =
            SimpleDateFormat("yyyy年M月d日(E) HH:mm").format(taskDataList[position].period_time)

        // 完了状態のチェック
        // 期限日付テキスト：
        // 期限が切れていた場合
        if (taskDataList[position].period_time.before(Timestamp(System.currentTimeMillis()))) {
            holder.periodDateTextView.setTextColor(Color.RED)
            // changeListener.onItemChange(taskDataList[position], taskDataList[position].status == TaskStatus.DONE.id)
        }
        // 完了になっている場合
        if (taskDataList[position].status == TaskStatus.DONE.id) {
            holder.periodDateTextView.setTextColor(Color.GREEN)
            // リスト上では完了状態になったら固定にする　詳細画面でのみ未完に戻せる
            holder.statusCheckBox.isEnabled = false
        } else {
            holder.statusCheckBox.isEnabled = true
        }

        if(taskDataList[position].status == TaskStatus.NO_START.id && taskDataList[position].period_time.after(Timestamp(System.currentTimeMillis()))){
            holder.periodDateTextView.setTextColor(Color.BLACK)
        }

        when (taskDataList[position].status) {
            TaskStatus.DONE.id -> holder.statusCheckBox.isChecked = true
            else -> holder.statusCheckBox.isChecked = false
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(taskDataList[position])
        }

        holder.statusCheckBox.setOnClickListener {
            AlertDialog.Builder(holder.statusCheckBox.context)
                .setTitle("確認")
                .setMessage("タスクを完了にしますか？")
                .setPositiveButton("はい") { dialog, which ->
                    var tmpTask = taskDataList[position]

                    holder.statusCheckBox.isEnabled = false
                    holder.statusCheckBox.isChecked = true
                    changeListener.onItemChange(taskDataList[position], true)

                    Snackbar
                        .make(holder.statusCheckBox, "タスク完了お疲れ様です！", Snackbar.LENGTH_SHORT)
                        .setAction("元に戻す") {
                            holder.statusCheckBox.isEnabled = true
                            holder.statusCheckBox.isChecked = false
                            changeListener.onItemChange(tmpTask, false)
                        }
                        .show()
                }
                .setNegativeButton("キャンセル") { dialog, which ->
                    holder.statusCheckBox.isChecked = false
                }
                .show()
        }

        holder.statusCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // TODO:ダイアログのリファクタリング
            // TODO:文字列のstring.xml化

        }
    }

    override fun getItemCount() = taskDataList.size

}