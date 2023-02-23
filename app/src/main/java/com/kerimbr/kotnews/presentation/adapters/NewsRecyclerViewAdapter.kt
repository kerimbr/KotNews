package com.kerimbr.kotnews.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kerimbr.kotnews.R
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.core.utils.extensions.getImageFromUrl
import com.kerimbr.kotnews.core.utils.extensions.parseISODate
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.databinding.ItemNewRowBinding
import com.kerimbr.kotnews.domain.usecase.NewsUseCases
import javax.inject.Inject

class NewsRecyclerViewAdapter : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemNewRowBinding) : BaseViewHolder(binding.root) {

        @Inject
        lateinit var newsUseCases: NewsUseCases

        override fun bind(item: Any) {
            val article: Article = item as Article
            binding.newRowTitle.text = article.title ?: "No title"
            binding.newRowContent.text = article.description ?: "No description"
            binding.newRowAuthor.text = article.author ?: "Unknown"
            binding.newRowDate.text = article.publishedAt?.parseISODate()
            article.urlToImage?.let { urlToImage ->
                binding.newRowImage.getImageFromUrl(urlToImage)
            }
        }

        override fun setOnClickListener(item: Any, onItemClickListener: OnItemClickListener?) {
            val article: Article = item as Article
            binding.newRowCard.setOnClickListener {
                onItemClickListener?.onItemClick(article)
            }
        }

        fun checkBookmark(currentArticle: Article, onItemBookmarkListener: OnItemBookmarkListener?) {

            val isMarked = onItemBookmarkListener?.onCheckBookmark(currentArticle)

            if (isMarked == true) {
                binding.newCardAddBookmarkButton.icon = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.baseline_bookmark_24
                )
            } else {
                binding.newCardAddBookmarkButton.icon = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.baseline_bookmark_border_24
                )
            }
        }

        fun bookmarkButtonClicked(
            currentArticle: Article,
            onItemBookmarkListener: OnItemBookmarkListener?,
            notifyCallback: () -> Unit) {
            binding.newCardAddBookmarkButton.setOnClickListener {
                val isMarked = onItemBookmarkListener?.setOnItemBookmarkClick(currentArticle)
                if (isMarked == true) {
                    binding.newCardAddBookmarkButton.icon = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.baseline_bookmark_24
                    )
                } else {
                    binding.newCardAddBookmarkButton.icon = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.baseline_bookmark_border_24
                    )
                }
                notifyCallback()
            }
        }

    }


    val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    private var onItemClickListener: BaseViewHolder.OnItemClickListener? = null
    private var onItemBookmarkListener: BaseViewHolder.OnItemBookmarkListener? = null



    fun setOnItemClickListener(onItemClickListener: BaseViewHolder.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemBookmarkListener(onItemBookmarkListener: BaseViewHolder.OnItemBookmarkListener) {
        this.onItemBookmarkListener = onItemBookmarkListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
        holder.setOnClickListener(article, onItemClickListener)
        holder.checkBookmark(article, onItemBookmarkListener)
        holder.bookmarkButtonClicked(article, onItemBookmarkListener){
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

}