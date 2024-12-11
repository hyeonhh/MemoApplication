package com.likeLion.memo.ui.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import kotlinx.coroutines.launch

class WriteMemoViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    // 메모 데이터 변수

    fun saveMemo(memoData : MemoItem) {
        viewModelScope.launch {
            dataStoreManager.saveMemo(memoData)
        }
    }
}