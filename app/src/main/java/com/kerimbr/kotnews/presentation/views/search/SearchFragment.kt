package com.kerimbr.kotnews.presentation.views.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.search.SearchView
import com.kerimbr.kotnews.core.base.BaseFragment
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.data.utils.Resource
import com.kerimbr.kotnews.databinding.FragmentSearchBinding
import com.kerimbr.kotnews.presentation.adapters.BookmarksRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.adapters.LastSearchRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.adapters.NewsRecyclerViewAdapter
import com.kerimbr.kotnews.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var newsRecyclerViewAdapter: NewsRecyclerViewAdapter

    @Inject
    lateinit var lastSearchRecyclerViewAdapter: LastSearchRecyclerViewAdapter

    @Inject
    lateinit var bookmarksRecyclerViewAdapter: BookmarksRecyclerViewAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, attachToParent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        initRecyclerView()
        initLastSearchRecyclerView()
        observeLastSearchQueries()
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModel.searchQuery.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                showSearchResults(it)
            }
        }
    }

    private fun showSearchResults(query: String) {
        viewModel.getNewsByQuery(query)
        viewModel.news.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.searchRecyclerView.visibility = View.GONE
                    showSearchLottie()
                }

                is Resource.Error -> {
                    binding.searchRecyclerView.visibility = View.GONE
                    hideSearchLottie()
                }

                is Resource.Success -> {
                    it.data?.let { res ->
                        binding.searchRecyclerView.visibility = View.VISIBLE
                        newsRecyclerViewAdapter.differ.submitList(res.articles?.toList())
                    }
                    hideSearchLottie()
                }
            }
        }
    }

    private fun initRecyclerView() {

        newsRecyclerViewAdapter.setOnItemClickListener(object :
            BaseViewHolder.OnItemClickListener {
            override fun onItemClick(item: Any) {
                val article = item as Article
                val navController = findNavController()
                val action =
                    SearchFragmentDirections.actionSearchFragmentToNewDetailFragment(article)
                navController.navigate(action)
            }
        })

        newsRecyclerViewAdapter.setOnItemBookmarkListener(object :
            BaseViewHolder.OnItemBookmarkListener {
            override fun setOnItemBookmarkClick(item: Any): Boolean {
                val article = item as Article
                val isMarked = onCheckBookmark(article)
                if (isMarked) {
                    viewModel.deleteBookmark(article)
                } else {
                    viewModel.addBookmark(article)
                }
                return isMarked
            }

            override fun onCheckBookmark(item: Any): Boolean {
                val article = item as Article
                return viewModel.checkBookmark(article)
            }
        })

        binding.searchRecyclerView.apply {
            adapter = newsRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLastSearchQueries() {
        viewModel.searchHistory.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.lastSearchRecyclerView.visibility = View.GONE
            } else {
                binding.lastSearchRecyclerView.visibility = View.VISIBLE
                lastSearchRecyclerViewAdapter.differ.submitList(it)
            }
        }
    }

    private fun initLastSearchRecyclerView() {

        lastSearchRecyclerViewAdapter.setOnItemClickListener(object :
            BaseViewHolder.OnItemClickListener {
            override fun onItemClick(item: Any) {
                val searchQuery = item as String
                setQuery(searchQuery)
            }
        })

        lastSearchRecyclerViewAdapter.setOnItemDeleteListener {
            viewModel.deleteSearchQueryFromHistory(it)
        }

        binding.lastSearchRecyclerView.apply {
            adapter = lastSearchRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initSearchView() {

        binding.searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.SHOWN) {
                viewModel.getSearchHistory()
            }
        }



        binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
            setQuery(textView.text.toString())
            false
        }
    }

    private fun setQuery(query: String) {
        binding.searchBar.text = query
        viewModel.setSearchQuery(query)
        binding.searchView.hide()
        viewModel.addSearchQueryToHistory(binding.searchBar.text.toString())
    }

    private fun showSearchLottie() {
        binding.searchLottie.visibility = View.VISIBLE
    }

    private fun hideSearchLottie() {
        binding.searchLottie.visibility = View.GONE
    }

}