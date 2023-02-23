package com.kerimbr.kotnews.presentation.views.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kerimbr.kotnews.R
import com.kerimbr.kotnews.core.base.BaseFragment
import com.kerimbr.kotnews.core.base.BaseViewHolder
import com.kerimbr.kotnews.data.models.news.Article
import com.kerimbr.kotnews.databinding.FragmentNewsBinding
import com.kerimbr.kotnews.presentation.adapters.NewsPagingDataAdapter
import com.kerimbr.kotnews.presentation.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var newsPagingDataAdapter: NewsPagingDataAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentNewsBinding {
        return FragmentNewsBinding.inflate(inflater, container, attachToParent)
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
        setTopAppBar()
        initRecyclerView()
        showNewsListWithPagination()
    }

    private fun setTopAppBar() {
        binding.topAppBar.title = "KotNews"
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.source_code -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Visit Source Code ?")
                        .setMessage("Do you want to open the source code of this app?")
                        .setPositiveButton("Yes") { _, _ ->
                            activity?.let { viewModel.openGithubRepo(it) }
                        }
                        .setNegativeButton("No") { _, _ -> }
                        .show()
                    true
                }
                else -> false
            }
        }
    }

    private fun showNewsListWithPagination() {
        // Load Data
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingNews.collectLatest {
                    newsPagingDataAdapter.submitData(it)
                }
            }
        }

        // Loading States
        lifecycleScope.launch {
            newsPagingDataAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading) {
                    showProgressBar()
                } else {
                    hideProgressBar()
                }
            }
        }

        // Error States
        lifecycleScope.launch {
            newsPagingDataAdapter.loadStateFlow.collectLatest { loadStates ->
                if (loadStates.refresh is LoadState.Error || loadStates.append is LoadState.Error) {
                    Toast.makeText(
                        activity,
                        "An error occured: ${loadStates.refresh as LoadState.Error}", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }


    private fun initRecyclerView() {

        newsPagingDataAdapter.setOnItemClickListener(object : BaseViewHolder.OnItemClickListener {
            override fun onItemClick(item: Any) {
                val article = item as Article
                val navController = findNavController()
                val action = NewsFragmentDirections.actionNewsFragmentToNewDetailFragment(article)
                navController.navigate(action)
            }
        })

        newsPagingDataAdapter.setOnItemBookmarkListener(object : BaseViewHolder.OnItemBookmarkListener {
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

        binding.newsRecyclerView.apply {
            adapter = newsPagingDataAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar() {
        binding.newsLinearProgressIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.newsLinearProgressIndicator.visibility = View.GONE
    }

}