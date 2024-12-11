package com.likeLion.memo.ui.modify

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ModifyMemoViewModel(private val dataStoreManager: DataStoreManager) : ViewModel(){

    private val _memoItem = MutableStateFlow(MemoItem())
    val memoItem = _memoItem.asStateFlow()

    fun getMemo(id : Int) {
        viewModelScope.launch {
            try {
                dataStoreManager.getMemoById(id)
                    .first()
                    .let {
                        if (it != null) _memoItem.value = it
                    }
            }catch (e:NullPointerException){
                e.printStackTrace()
        }
        }
    }

    fun modifyMemo(id : Int, memo:MemoItem) {
        viewModelScope.launch {
            try {
                dataStoreManager.updateMemo(id, memo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}