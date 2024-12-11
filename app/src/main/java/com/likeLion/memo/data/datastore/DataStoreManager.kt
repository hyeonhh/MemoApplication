package com.likeLion.memo.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.likeLion.memo.data.model.MemoItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray

class DataStoreManager private  constructor(private val context: Context){
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "memo")
    private val dataStore = context.dataStore

    companion object {
        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context.applicationContext).also {
                    instance = it
                }
            }
        }


        val MEMO_LIST = stringPreferencesKey("memo_list")
        val LAST_ID = intPreferencesKey("last_memo_id")  // 마지막 ID 추적용

    }

    // 현재 메모 아이템 가져오기
    fun getMemoById(id: Int): Flow<MemoItem?> =
        dataStore.data.map { preferences ->
            val memoListString = preferences[MEMO_LIST] ?: return@map null
            try {
                val jsonArray = JSONArray(memoListString)
                for (i in 0..jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    if (jsonObject.getInt("id") == id) {
                        return@map MemoItem(
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            content = jsonObject.getString("content"),
                            date = jsonObject.getString("date")
                        )
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


    fun getCurrentMemos(): Flow<List<MemoItem>> =
        dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preferences ->
                val memoListString = preferences[MEMO_LIST] ?: "[]"
                try {
                    val jsonArray = JSONArray(memoListString)
                    val memoList = mutableListOf<MemoItem>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        memoList.add(
                            MemoItem(
                                id = jsonObject.getInt("id"),
                                title = jsonObject.getString("title"),
                                content = jsonObject.getString("content"),
                                date = jsonObject.getString("date")
                            )
                        )
                    }
                    println("Datastore :$memoList")
                    memoList

                } catch (e: Exception) {
                    emptyList()
                }
            }


    suspend fun saveMemo(memoItem : MemoItem) {
        dataStore.edit { preferences ->
            val currentLastId = preferences[LAST_ID] ?: -1
            val newId = currentLastId + 1

            val newMemo = MemoItem(
                id = newId,
                title = memoItem.title,
                content = memoItem.content,
                date = memoItem.date
            )

            val memoListString = preferences[MEMO_LIST] ?: "[]"
            val currentMemos = try {
                val jsonArray = JSONArray(memoListString)
                val memoList = mutableListOf<MemoItem>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    memoList.add(
                        MemoItem(
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            content = jsonObject.getString("content"),
                            date = jsonObject.getString("date")
                        )
                    )
                }
                memoList
            } catch (e: Exception) {
                mutableListOf()
            }

            currentMemos.add(newMemo)
            preferences[MEMO_LIST] = Gson().toJson(currentMemos)
            preferences[LAST_ID] = newId
        }


    }

suspend fun updateMemo(id : Int,  memo: MemoItem) {
    // 현재 메모리스트 가져오기
    /*
    1. coroutineScope를 사용하지 않을 경우
    2. 그냥 collect를 해줄 경우
     */
    coroutineScope {
        getCurrentMemos()
            .first()
            .let {
                val mutableList = it.toMutableList()
                val index = mutableList.indexOfFirst { it.id == id }

                if (index != -1) {
                   mutableList[index] = memo.copy(
                       id = memo.id, title = memo.title,
                       content = memo.content, date = memo.date
                      )
                    dataStore.edit {
                        it[MEMO_LIST] = Gson().toJson(mutableList)
                    }
                }
            }
    }
}

    suspend fun deleteMemo(id: Int) {
        coroutineScope {
            getCurrentMemos()
                .first()
                .let{
                val mutableList = it.toMutableList()

                val index = mutableList.indexOfFirst { it.id == id }
                if (index != -1) {
                    mutableList.removeAt(index)
                }

                // 수정된 리스트 저장
                dataStore.edit {
                    it[MEMO_LIST] = Gson().toJson(mutableList)
                }

            }
        }


    }
}