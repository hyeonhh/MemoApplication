package com.likeLion.memo.ui.show

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likeLion.memo.data.datastore.DataStoreManager
import com.likeLion.memo.data.model.MemoItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ShowMemoViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    private val _memoData = MutableStateFlow(MemoItem())
    val memoData = _memoData.asStateFlow()


    fun getMemoById(id : Int) {
        viewModelScope.launch {
            dataStoreManager.getMemoById(id).collect {
                if (it!= null ){
                    _memoData.value= it
                }
                else {
                    Log.e("modify failed","failed to modify")
                }
            }
        }
    }

    fun deleteMemo(id : Int) {
        viewModelScope.launch {
            dataStoreManager.deleteMemo(id)
            }
        }

}