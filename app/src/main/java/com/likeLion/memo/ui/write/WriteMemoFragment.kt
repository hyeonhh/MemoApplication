package com.likeLion.memo.ui.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.likeLion.memo.R
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import com.likeLion.memo.databinding.FragmentWriteMemoBinding
import com.likeLion.memo.ui.date.CalendarFragment
import com.likeLion.memo.ui.date.DateSelectListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class WriteMemoFragment : Fragment() {

    private var _binding : FragmentWriteMemoBinding? = null
    private val binding get() = _binding!!

    private val dataStoreManager by lazy {
        DataStoreManager.getInstance(requireContext())
    }

    private val viewModel: WriteMemoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WriteMemoViewModel(dataStoreManager) as T
            }
        }
    }

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
        binding.btnModifyDate.setOnClickListener {
            showCalendarDialog()
        }

        binding.btnComplete.setOnClickListener {
            viewModel.saveMemo(MemoItem(
                title = binding.editMemoTitle.text.toString(),
                date = binding.txtCurrentDate.text.toString(),
                content = binding.editMemoContent.text.toString()
            ))

            Toast.makeText(context, "메모를 작성했어요!",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.showMemoListFragment)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCalendarDialog() {
        CalendarFragment().apply {
            setDateSelectListener(object : DateSelectListener {
                override fun onDateSelected(date: String) {
                    binding.txtCurrentDate.text= date
                }
            })
        }.show(childFragmentManager,"CalendarDialog")
    }


}