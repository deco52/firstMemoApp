package com.example.firstmemoapp.view.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * 日付取得ダイアログ
 * 参考：https://tech-blog.re-arc-lab.jp/posts/200903_android-datetimepicker/
 */
class DatePick private constructor() : DialogFragment(), DatePickerDialog.OnDateSetListener {
    interface DatePickerDialogListener {
        fun onDateSet(year: Int, month: Int, dayOfMonth: Int)
    }

    companion object {
        private const val ARG_YEAR = "ARG_YEAR"
        private const val ARG_MONTH = "ARG_MONTH"
        private const val ARG_DAY_OF_MONTH = "ARG_DAY_OF_MONTH"

        @JvmStatic
        fun newInstance(year: Int, month: Int, dayOfMonth: Int): DatePick {
            return DatePick().apply {
                arguments = Bundle().apply {
                    putInt(ARG_YEAR, year)
                    putInt(ARG_MONTH, month)
                    putInt(ARG_DAY_OF_MONTH, dayOfMonth)
                }
            }
        }
        @JvmStatic
        fun newInstance(): DatePick {
            return DatePick()
        }
    }

    private lateinit var listener: DatePickerDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (parentFragment) {
            is DatePickerDialogListener -> listener = parentFragment as DatePickerDialogListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        if (arguments != null) {
            year = requireArguments().getInt(ARG_YEAR)
            month = requireArguments().getInt(ARG_MONTH) - 1
            dayOfMonth = requireArguments().getInt(ARG_DAY_OF_MONTH)
        }
        return DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        listener.onDateSet(year, month + 1, dayOfMonth)
    }
}