package com.elanyudho.newsapp.ui.search

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.core.model.model.Article
import com.elanyudho.core.model.model.Source
import com.elanyudho.core.pref.EncryptedPreferences
import com.elanyudho.core.util.exception.Failure
import com.elanyudho.core.util.pagination.RecyclerViewPaginator
import com.elanyudho.newsapp.R
import com.elanyudho.newsapp.databinding.ActivitySearchBinding
import com.elanyudho.newsapp.ui.articles.ArticlesActivity
import com.elanyudho.newsapp.ui.articles.ArticlesActivity.Companion.SOURCE_ID
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity.Companion.SOURCE_NEWS
import com.elanyudho.newsapp.ui.detail.DetailNewsActivity.Companion.URL_NEWS
import com.elanyudho.newsapp.ui.main.MainActivity.Companion.SEARCH_ARTICLE
import com.elanyudho.newsapp.ui.main.MainActivity.Companion.SEARCH_SOURCE
import com.elanyudho.newsapp.ui.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : BaseActivityBinding<ActivitySearchBinding>(),
    Observer<SearchViewModel.SearchUiState> {

    private lateinit var searchQuery: String

    @Inject
    lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var encryptedPreferences: EncryptedPreferences

    private val searchAdapter: SearchAdapter by lazy { SearchAdapter() }

    private var paginator: RecyclerViewPaginator? = null

    override val bindingInflater: (LayoutInflater) -> ActivitySearchBinding
        get() = { ActivitySearchBinding.inflate(layoutInflater) }

    override fun setupView() {
        getIntentData()
        initViewModel()
        setAdapter()
        setAction()
        setPagination()
    }

    override fun onChanged(value: SearchViewModel.SearchUiState) {
        setView()

        when (value) {
            is SearchViewModel.SearchUiState.SourcesLoaded -> {
                stopLoading()
                if (value.data.isEmpty() && paginator?.isFirstGet == true) {
                    showEmptyData()
                }else {
                    hideEmptyData()
                    searchAdapter.appendList(value.data)
                }
            }

            is SearchViewModel.SearchUiState.ArticleLoaded -> {
                stopLoading()
                if (value.data.isEmpty() && paginator?.isFirstGet == true) {
                    showEmptyData()
                }else {
                    hideEmptyData()
                    searchAdapter.appendList(value.data)
                }
            }

            is SearchViewModel.SearchUiState.InitialLoading -> {
                initialLoading()
            }

            is SearchViewModel.SearchUiState.PagingLoading -> {
                pagingLoading()
            }

            is SearchViewModel.SearchUiState.FailedLoaded -> {
                stopLoading()
                handleFailure(value.failure)
            }
        }
    }

    private fun getIntentData() {
        searchQuery = intent.getStringExtra(SEARCH_QUERY) ?: ""
    }

    private fun initViewModel() {
        searchViewModel.uiState.observe(this, this)
        if (encryptedPreferences.filter == SEARCH_SOURCE) {
            searchViewModel.getSourceByName(searchQuery, 1)
        } else {
            searchViewModel.getArticlesByQuery(searchQuery, 1)
        }
    }

    private fun setView() {
        binding.tvResult.text =  getString(R.string.result_text, searchQuery)
    }

    private fun setAction() {
        with(binding) {
            svNews.setQuery(searchQuery)
            svNews.setOnQueryChangeListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard(svNews)
                    if (query.toString().isNotEmpty()) {
                        searchQuery = query!!
                        if (encryptedPreferences.filter == SEARCH_SOURCE) {
                            searchViewModel.getSourceByName(searchQuery, 1)
                            searchAdapter.setTypeAdapter(SEARCH_SOURCE)
                            searchAdapter.clearList()
                            binding.rvNews.adapter = searchAdapter
                        } else {
                            searchViewModel.getArticlesByQuery(searchQuery, 1)
                            searchAdapter.setTypeAdapter(SEARCH_ARTICLE)
                            searchAdapter.clearList()
                            binding.rvNews.adapter = searchAdapter
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    return false
                }

            })

            svNews.setOnAdditionalButtonListener {
                showFilterDialog()
            }

            btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        }
    }

    private fun setAdapter() {
        with(binding.rvNews) {
            searchAdapter.setTypeAdapter(encryptedPreferences.filter)
            adapter = searchAdapter
            setHasFixedSize(true)

            searchAdapter.setOnClickData { it, filter ->
                if (filter == SEARCH_SOURCE) {
                    val data = it as Source
                    val intent = Intent(this@SearchActivity, ArticlesActivity::class.java)
                    intent.putExtra(SOURCE_ID, data.id)
                    startActivity(intent)
                } else {
                    val data = it as Article
                    val intent = Intent(this@SearchActivity, DetailNewsActivity::class.java)
                    intent.putExtra(SOURCE_NEWS, it.source)
                    intent.putExtra(URL_NEWS, data.url)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setPagination() {
        paginator = RecyclerViewPaginator(binding.rvNews.layoutManager as LinearLayoutManager)
        paginator?.setOnLoadMoreListener { page ->
            paginator?.isFirstGet = false
            if (encryptedPreferences.filter == SEARCH_SOURCE) {
                searchViewModel.getSourceByName(searchQuery, page)
            } else {
                searchViewModel.getArticlesByQuery(searchQuery, page)
            }
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

    private fun showFilterDialog() {
        val filterOptions = arrayOf(getString(R.string.search_for_source), getString(R.string.search_for_article))
        var filter = encryptedPreferences.filter
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.search_for))
            .setSingleChoiceItems(filterOptions, if (filter == SEARCH_SOURCE) 0 else 1) { _, which ->
                when (which) {
                    0 -> {
                        filter = SEARCH_SOURCE
                    }
                    1 -> {
                        filter = SEARCH_ARTICLE
                    }
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.apply)) { dialog, _ ->
                encryptedPreferences.filter = filter
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        const val SEARCH_QUERY = "search_query"
    }

}