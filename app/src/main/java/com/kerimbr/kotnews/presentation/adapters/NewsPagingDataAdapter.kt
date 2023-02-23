package com.kerimbr.kotnews.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.databinding.ItemNewRowBinding

class NewsPagingDataAdapter : PagingDataAdapter<Article, NewsRecyclerViewAdapter.ViewHolder>(
    NewsRecyclerViewAdapter.DIFF_CALLBACK
) {

    private var onItemClickListener: BaseViewHolder.OnItemClickListener? = null
    private var onItemBookmarkListener: BaseViewHolder.OnItemBookmarkListener? = null


    fun setOnItemClickListener(onItemClickListener: BaseViewHolder.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemBookmarkListener(onItemBookmarkListener: BaseViewHolder.OnItemBookmarkListener) {
        this.onItemBookmarkListener = onItemBookmarkListener
    }

    override fun onBindViewHolder(holder: NewsRecyclerViewAdapter.ViewHolder, position: Int) {
        val article = getItem(position)
        article?.let {
            holder.bind(it)
            holder.setOnClickListener(it, onItemClickListener)
            holder.checkBookmark(article, onItemBookmarkListener)
            holder.bookmarkButtonClicked(article, onItemBookmarkListener){
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsRecyclerViewAdapter.ViewHolder {
        val binding = ItemNewRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsRecyclerViewAdapter.ViewHolder(binding)
    }

}