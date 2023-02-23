package com.kerimbr.kotnews.core.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: Any)

    open fun setOnClickListener(item: Any, onItemClickListener: OnItemClickListener?){
        itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any)
    }

    interface OnItemBookmarkListener {
        fun setOnItemBookmarkClick(item: Any): Boolean

        fun onCheckBookmark(item: Any): Boolean

    }

}