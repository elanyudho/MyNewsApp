package com.elanyudho.newsapp.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.lifecycleScope
import com.elanyudho.core.abstraction.BaseActivityBinding
import com.elanyudho.newsapp.R
import com.elanyudho.newsapp.databinding.ActivityDetailNewsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@AndroidEntryPoint
class DetailNewsActivity : BaseActivityBinding<ActivityDetailNewsBinding>() {

    private lateinit var urlNews: String
    private lateinit var sourceNews: String

    override val bindingInflater: (LayoutInflater) -> ActivityDetailNewsBinding
        get() = { ActivityDetailNewsBinding.inflate(layoutInflater) }

    override fun setupView() {
        getDataIntent()
        setHeader()
        setWebView()
    }

    private fun getDataIntent() {
        urlNews = intent.getStringExtra(URL_NEWS) ?: ""
        sourceNews = intent.getStringExtra(SOURCE_NEWS) ?: ""
    }

    private fun setHeader() {
        with(binding) {
            headerDetail.tvHeader.text = sourceNews
            headerDetail.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        binding.webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    if (!this@DetailNewsActivity.isDestroyed) {
                        binding.progressCircular.visibility = View.GONE
                    }
                }
            }
        }
        binding.webview.loadUrl(urlNews)
    }

    companion object {
        const val URL_NEWS = "url_news"
        const val SOURCE_NEWS = "source_news"
    }

}