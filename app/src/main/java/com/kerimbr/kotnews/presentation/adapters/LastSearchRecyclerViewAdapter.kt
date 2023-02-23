package com.kerimbr.kotnews.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.databinding.ItemLastSearchRowBinding

class LastSearchRecyclerViewAdapter: RecyclerView.Adapter<LastSearchRecyclerViewAdapter.ViewHolder>(){

    class ViewHolder(private val binding : ItemLastSearchRowBinding) : BaseViewHolder(binding.root) {
        override fun bind(item: Any) {
            val searchQuery = item as String
            binding.lastSearchText.text = searchQuery
        }

        override fun setOnClickListener(item: Any, onItemClickListener: OnItemClickListener?) {
            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
        }

        fun setOnDeleteClickListener(item: Any, onItemDeleteListener: ((String) -> Unit)?){
            binding.lastSearchDeleteButton.setOnClickListener {
                onItemDeleteListener?.invoke(item as String)
            }
        }

    }

    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    private var onItemClickListener: BaseViewHolder.OnItemClickListener? = null
    private var onItemDeleteListener: ((String) -> Unit)? = null

    fun setOnItemDeleteListener(onItemDeleteListener: (String) -> Unit){
        this.onItemDeleteListener = onItemDeleteListener
    }

    fun setOnItemClickListener(onItemClickListener: BaseViewHolder.OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLastSearchRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchQuery = differ.currentList[position]
        holder.bind(searchQuery)
        holder.setOnClickListener(searchQuery, onItemClickListener)
        holder.setOnDeleteClickListener(searchQuery, onItemDeleteListener)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}