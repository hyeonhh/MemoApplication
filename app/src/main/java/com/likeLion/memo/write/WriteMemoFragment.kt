package com.likeLion.memo.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.likeLion.memo.databinding.FragmentWriteMemoBinding
import com.likeLion.memo.date.CalendarFragment
import com.likeLion.memo.date.DateSelectListener
import java.text.SimpleDateFormat
import java.util.Date

class WriteMemoFragment : Fragment() {

    private var _binding : FragmentWriteMemoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteMemoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 기본 : 현재 날짜
        val now = System.currentTimeMillis()
        val date = Date(now)
        val dataFormat = SimpleDateFormat("yyyy-MM-dd")
        val getDate = dataFormat.format(date)
        binding.txtCurrentDate.text  = getDate
        binding.btnModifyDate.setOnClickListener{
            // todo : 캘린더뷰 다이얼로그 띄우기
            showCalendarDialog()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCalendarDialog() {
        val calendarDialog = CalendarFragment().apply {
            setDateSelectListener(object : DateSelectListener {
                override fun onDateSelected(date: String) {
                    binding.txtCurrentDate.text= date
                }
            })
        }.show(childFragmentManager,"CalendarDialog")
    }


}