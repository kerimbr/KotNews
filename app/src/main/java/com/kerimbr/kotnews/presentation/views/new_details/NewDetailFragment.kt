package com.kerimbr.kotnews.presentation.views.new_details

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kerimbr.kotnews.R
import com.kerimbr.kotnews.core.base.BaseFragment
import com.kerimbr.kotnews.databinding.FragmentNewDetailBinding
import com.kerimbr.kotnews.presentation.viewmodels.NewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewDetailFragment : BaseFragment<FragmentNewDetailBinding>() {


    private val viewModel: NewDetailViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToParent: Boolean
    ): FragmentNewDetailBinding {
        return FragmentNewDetailBinding.inflate(inflater, container, attachToParent)
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
        setTopAppBarClickListeners()
        bindNewDetail()
        observeArticle()
        observeIsBookmarked()
    }

    private fun observeIsBookmarked() {
        viewModel.isBookmarked.observe(viewLifecycleOwner) { isBookmarked ->
            if (isBookmarked == true) {
                binding.newDetailTopAppBar.menu.findItem(R.id.bookmark_icon)
                    .setIcon(R.drawable.baseline_bookmark_24)
            } else {
                binding.newDetailTopAppBar.menu.findItem(R.id.bookmark_icon)
                    .setIcon(R.drawable.baseline_bookmark_border_24)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun observeArticle() {
        viewModel.article.observe(viewLifecycleOwner) { article ->
            if (article == null){
                binding.newDetailWebView.visibility = View.GONE
            }else{
                binding.newDetailTopAppBar.title = article.title ?: ""
                binding.newDetailTopAppBar.subtitle = article.url ?: ""

                binding.newDetailWebView.apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    article.url?.let { articleUrl ->
                        loadUrl(articleUrl)
                    }

                }
                binding.newDetailWebView.visibility = View.VISIBLE
            }
        }
    }

    private fun setTopAppBarClickListeners() {
        binding.newDetailTopAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }



        binding.newDetailTopAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share_icon -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, viewModel.article.value?.url)
                    startActivity(Intent.createChooser(intent, "Share New"))
                    true
                }

                R.id.bookmark_icon -> {
                    if (viewModel.isBookmarked.value == true) {
                        menuItem.setIcon(R.drawable.baseline_bookmark_border_24)
                        viewModel.removeBookmark()
                    } else {
                        menuItem.setIcon(R.drawable.baseline_bookmark_24)
                        viewModel.addBookmark()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun bindNewDetail() {
        arguments?.let {
            val args = NewDetailFragmentArgs.fromBundle(it)
            viewModel.setArticle(args.article)
        }
    }


}