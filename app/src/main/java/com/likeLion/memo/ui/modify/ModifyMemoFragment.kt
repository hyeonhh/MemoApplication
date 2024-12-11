package com.likeLion.memo.ui.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.likeLion.memo.R
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import com.likeLion.memo.databinding.FragmentModifyMemoBinding
import com.likeLion.memo.ui.date.CalendarFragment
import com.likeLion.memo.ui.date.DateSelectListener
import com.likeLion.memo.ui.show.ShowMemoFragmentDirections
import kotlinx.coroutines.launch

class ModifyMemoFragment  : Fragment(){

    private var _binding : FragmentModifyMemoBinding? = null
    private val binding get() = _binding!!

    private val args: ModifyMemoFragmentArgs by navArgs()

    private val dataStoreManager by lazy {
        DataStoreManager.getInstance(requireContext())
    }
    private val viewModel : ModifyMemoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ModifyMemoViewModel(dataStoreManager) as T
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModifyMemoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMemo(args.id)

        binding.btnChangeDate.setOnClickListener {
            showCalendarDialog()
        }

        binding.btnComplete.setOnClickListener {
            viewModel.modifyMemo(args.id, MemoItem(
                title = binding.editMemoTitle.text.toString(),
                content = binding.editMemoContent.text.toString(),
                date = binding.txtDate.text.toString()
                    ))

            Toast.makeText(context,"메모를 수정했어요!",Toast.LENGTH_SHORT).show()

            // 메모 화면으로 이동
            val action = ModifyMemoFragmentDirections.actionModifyMemoFragmentToShowMemoFragment(args.id)
            findNavController().navigate(action)
        }

        // todo : 여기서 NullPointerException 발생
        lifecycleScope.launch {
            viewModel.memoItem.collect{
                binding.editMemoTitle.setText(it.title)
                binding.editMemoContent.setText(it.content)
                binding.txtDate.text = it.date
            }
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
                    binding.txtDate.text= date
                }
            })
        }.show(childFragmentManager,"CalendarDialog")
    }
}