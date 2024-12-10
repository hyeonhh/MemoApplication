package com.likeLion.memo.date

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.likeLion.memo.databinding.FragmentCalendarDialogBinding


interface DateSelectListener {
    fun onDateSelected(date: String)
}

class CalendarFragment : DialogFragment() {
    private var dateSelectListener: DateSelectListener? = null
    private var _binding: FragmentCalendarDialogBinding? = null
    private val binding get() = _binding!!

    private var selectedDate : String? = null

    fun setDateSelectListener(listener: DateSelectListener) {
        dateSelectListener = listener
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            selectedDate = "$i-$i2-$i3"
        }

        binding.btnComplete.setOnClickListener{
            selectedDate?.let { date ->
                dateSelectListener?.onDateSelected(date)
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this@CalendarFragment,0.9f,0.8f)

    }

    private fun Context.dialogFragmentResize(
        dialogFragment  : DialogFragment,
        width: Float,
        height : Float,
    ) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x,y)
        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val window = dialogFragment.dialog?.window
            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x,y)
        }
    }

}