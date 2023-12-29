package com.elanyudho.newsapp.ui.articles

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.pagination.RecyclerViewPaginator
import com.elanyudho.newsapp.R
import com.elanyudho.newsapp.databinding.ActivityArticlesBinding
import com.elanyudho.newsapp.ui.articles.adapter.ArticlesAdapter
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity.Companion.SOURCE_NEWS
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity.Companion.URL_NEWS
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticlesActivity : BaseActivityBinding<ActivityArticlesBinding>(), Observer<ArticlesViewModel.ArticlesUiState> {

    @Inject
    lateinit var articlesViewModel: ArticlesViewModel

    private val articlesAdapter: ArticlesAdapter by lazy { ArticlesAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    private lateinit var sourceId: String

    override val bindingInflater: (LayoutInflater) -> ActivityArticlesBinding
        get() = { ActivityArticlesBinding.inflate(layoutInflater) }

    override fun setupView() {
        getDataIntent()
        initViewModel()
        setHeader()
        setAdapter()
        setPagination()

    }

    override fun onChanged(value: ArticlesViewModel.ArticlesUiState) {
        when(value) {
            is ArticlesViewModel.ArticlesUiState.ArticlesLoaded -> {
                stopLoading()
                if (value.data.isEmpty() && paginator?.isFirstGet == true) {
                    showEmptyData()
                }else {
                    hideEmptyData()
                    articlesAdapter.appendList(value.data)
                }
            }
            is ArticlesViewModel.ArticlesUiState.InitialLoading -> {
                initialLoading()
            }
            is ArticlesViewModel.ArticlesUiState.PagingLoading -> {
                pagingLoading()
            }
            is ArticlesViewModel.ArticlesUiState.FailedLoaded -> {
                stopLoading()
                handleFailure(value.failure)
            }
        }
    }

    private fun getDataIntent() {
        sourceId = intent.getStringExtra(SOURCE_ID) ?: ""
    }

    private fun initViewModel() {
        articlesViewModel.uiState.observe(this, this)
        articlesViewModel.getArticles(sourceId, 1)
    }

    private fun setHeader() {
        with(binding) {
            headerDetail.tvHeader.text = getString(R.string.article)
            headerDetail.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun setAdapter() {
        with(binding.rvNews) {
            adapter = articlesAdapter
            setHasFixedSize(true)

            articlesAdapter.setOnClickData {
                val intent = Intent(this@ArticlesActivity, DetailNewsActivity::class.java)
                intent.putExtra(SOURCE_NEWS, it.source)
                intent.putExtra(URL_NEWS, it.url)
                startActivity(intent)
            }
        }
    }

    private fun setPagination() {
        paginator = RecyclerViewPaginator(binding.rvNews.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            paginator?.isFirstGet = false
            articlesViewModel.getArticles(sourceId, page)
        }
        paginator?.let { binding.rvNews.addOnScrollListener(it) }
    }
    private fun initialLoading() {
        binding.progressCircular.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE
    }

    private fun pagingLoading() {
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.rvNews.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
    }

    private fun showEmptyData() {
        binding.emptyDataView.parent.visibility = View.VISIBLE
    }

    private fun hideEmptyData() {
        binding.emptyDataView.parent.visibility = View.GONE
    }

    private fun handleFailure(failure: Failure) {
        Toast.makeText(this, failure.throwable.message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SOURCE_ID = "source_id"
    }
}