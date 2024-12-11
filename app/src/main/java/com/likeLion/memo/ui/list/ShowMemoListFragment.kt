package com.likeLion.memo.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.likeLion.memo.R
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.databinding.FragmentShowMemoListBinding
import com.likeLion.memo.databinding.FragmentWriteMemoBinding
import com.likeLion.memo.ui.write.WriteMemoViewModel
import kotlinx.coroutines.launch




class ShowMemoListFragment : Fragment() {

    private var _binding : FragmentShowMemoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : MemoListAdapter
    private val dataStoreManager by lazy {
        DataStoreManager.getInstance(requireContext())
    }

    private val viewModel: ShowMemoListViewModel by viewModels {
        object :ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShowMemoListViewModel(dataStoreManager) as T
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowMemoListBinding.inflate(inflater, container, false)
        val view = binding.root

        adapter = MemoListAdapter()
        adapter.setMemoListener(
            object  : MemoListener {
                override fun onMemoClick(id: Int) {
                    // 클릭된 아이템의 id값도 함께 넘겨주기
                    val action = ShowMemoListFragmentDirections.actionShowMemoListFragmentToShowMemoFragment(id)
                    findNavController().navigate(action)
                }
            }
        )



        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemoList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.memoRecyclerview.adapter= adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.memoList.collect{
                    if (it.isNotEmpty()) {
                        adapter.setMemoList(it)
                    }
                }
            }

        }

        binding.btnFloating.setOnClickListener {
            findNavController().navigate(R.id.action_showMemoListFragment_to_writeMemoFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}