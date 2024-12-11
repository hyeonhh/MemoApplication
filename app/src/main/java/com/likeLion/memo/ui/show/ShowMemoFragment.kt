package com.likeLion.memo.ui.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.databinding.FragmentShowMemoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShowMemoFragment : Fragment() {

    private var _binding : FragmentShowMemoBinding? = null
    private val binding get() = _binding!!
    private val dataStoreManager by lazy {
        DataStoreManager.getInstance(requireContext())
    }

    private val args: ShowMemoFragmentArgs by navArgs()

    private var id : Int = 0
    private val viewModel : ShowMemoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShowMemoViewModel(dataStoreManager) as T
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowMemoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMemoById(args.id)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.memoData.collect {
                    binding.txtMemoTitle.text = it.title
                    binding.txtDate.text = it.date
                    binding.txtMemoContent.text = it.content
                    id= it.id
                }
            }

        }
        with(binding) {
            btnModify.setOnClickListener {
                val action = ShowMemoFragmentDirections.actionShowMemoFragmentToModifyMemoFragment(args.id)
                findNavController().navigate(action)
            }
            btnDelete.setOnClickListener {
                viewModel.deleteMemo(args.id)
                Toast.makeText(context,"메모를 삭제했어요!",Toast.LENGTH_SHORT).show()
                val action = ShowMemoFragmentDirections.actionShowMemoFragmentToShowMemoListFragment()
                findNavController().navigate(action)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}