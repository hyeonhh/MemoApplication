package com.likeLion.memo.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likeLion.memo.data.model.MemoItem
import com.likeLion.memo.databinding.ItemMemoBinding


interface MemoListener{
    fun onMemoClick(id : Int)
}

class MemoListAdapter : RecyclerView.Adapter<MemoListAdapter.MemoListViewHolder>() {
    private var memoList : MutableList<MemoItem> = mutableListOf()
    private var memoListener : MemoListener? = null

    // 리스너 설정 메서드 추가
    fun setMemoListener(listener: MemoListener) {
        this.memoListener = listener
    }


    fun setMemoList(newMemoList : List<MemoItem>) {
        this.memoList = newMemoList.toMutableList()
        Log.d("어댑터의 리스트","${this.memoList}")
        notifyDataSetChanged()
    }

    inner class MemoListViewHolder(private val binding : ItemMemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : MemoItem) {
            binding.txtMemoTitle.text= item.title.toString()
        }

        init {
            binding.memoCard.setOnClickListener {
                memoListener?.onMemoClick(memoList[position].id)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoListViewHolder {
       val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MemoListViewHolder(binding)
    }

    override fun getItemCount(): Int  = memoList.size

    override fun onBindViewHolder(holder: MemoListViewHolder, position: Int) {
        holder.bind(memoList[position])

    }
}