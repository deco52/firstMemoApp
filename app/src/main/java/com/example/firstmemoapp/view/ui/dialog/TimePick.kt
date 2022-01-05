package com.example.firstmemoapp.view.ui.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * 時間取得ダイアログ
 * 参考：https://tech-blog.re-arc-lab.jp/posts/200903_android-datetimepicker/
 */
class TimePick private constructor() : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface TimePickerDialogListener {
        fun onTimeSet(hourOfDay: Int, minute: Int)
    }

    companion object {
        private const val ARG_HOUR_OF_DAY = "ARG_HOUR_OF_DAY"
        private const val ARG_MINUTE = "ARG_MINUTE"

        @JvmStatic
        fun newInstance(hourOfDay: Int, minute: Int): TimePick {
            return TimePick().apply {
                arguments = Bundle().apply {
                    putInt(ARG_HOUR_OF_DAY, hourOfDay)
                    putInt(ARG_MINUTE, minute)
                }
            }
        }
        @JvmStatic
        fun newInstance(): TimePick {
            return TimePick()
        }
    }

    private lateinit var listener: TimePickerDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (parentFragment) {
            is TimePickerDialogListener -> listener = parentFragment as TimePickerDialogListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        var minute = calendar.get(Calendar.MINUTE)
        if (arguments != null) {
            hour = requireArguments().getInt(ARG_HOUR_OF_DAY)
            minute = requireArguments().getInt(ARG_MINUTE)
        }
        return TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener.onTimeSet(hourOfDay, minute)
    }
}