package com.likeLion.memo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class ShowMemoListViewModel (private val dataStoreManager: DataStoreManager): ViewModel() {

        // 메모 데이터 변수
        private val _memoList = MutableStateFlow<List<MemoItem>>(emptyList())
        val memoList = _memoList.asStateFlow()

        fun getMemoList() {
                viewModelScope.launch {
                        dataStoreManager.getCurrentMemos().collect {
                                _memoList.value = it
                                println(_memoList.value)
                        }
                }
        }

}