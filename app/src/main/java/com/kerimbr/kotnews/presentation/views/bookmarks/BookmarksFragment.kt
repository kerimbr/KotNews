package com.kerimbr.kotnews.presentation.views.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerimbr.kotnews.core.base.BaseFragment
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.databinding.FragmentBookmarksBinding
import com.kerimbr.kotnews.presentation.adapters.BookmarksRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.viewmodels.BookmarksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarksFragment : BaseFragment<FragmentBookmarksBinding>() {

    private val viewModel: BookmarksViewModel by viewModels()


    @Inject
    lateinit var bookmarksRecyclerViewAdapter: BookmarksRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentBookmarksBinding {
        return FragmentBookmarksBinding.inflate(inflater, container, attachToParent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = getViewBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeBookmarks()
        setBookmarkClickListener()
    }

    private fun setBookmarkClickListener() {
        bookmarksRecyclerViewAdapter.setOnItemBookmarkListener(
            object : BaseViewHolder.OnItemBookmarkListener {
                override fun setOnItemBookmarkClick(item: Any): Boolean {
                    val article = item as Article
                    lifecycleScope.launch {
                        val result = viewModel.deleteSavedNews(article)
                        if (result){
                            Toast.makeText(requireContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return false
                }

                override fun onCheckBookmark(item: Any): Boolean = true
            }
        )
    }

    private fun observeBookmarks() {
        viewModel.getAllBookmarks().observe(viewLifecycleOwner) {
            if (it.isEmpty()){
                binding.bookmarksRecyclerView.visibility = View.GONE
                binding.bookmarksEmptyView.visibility = View.VISIBLE
            }else{
                binding.bookmarksRecyclerView.visibility = View.VISIBLE
                binding.bookmarksEmptyView.visibility = View.GONE
            }
        }
    }



    private fun initRecyclerView() {
        bookmarksRecyclerViewAdapter.setOnItemClickListener(
            object : BaseViewHolder.OnItemClickListener {
                override fun onItemClick(item: Any) {
                    val article = item as Article
                    val action = BookmarksFragmentDirections.actionBookmarksFragmentToNewDetailFragment(article)
                    findNavController().navigate(action)
                }
            }
        )

        binding.bookmarksRecyclerView.apply {
            adapter = bookmarksRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }



}