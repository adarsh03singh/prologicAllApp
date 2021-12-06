package com.prologic.strains.view.activity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.prologic.strains.R
import com.prologic.strains.databinding.MyWebViewBinding
import com.prologic.strains.utils.AlertError
import com.prologic.strains.utils.OnDialogCloseListener
import com.prologic.strains.utils.isNetworkAvailable
import com.prologic.strains.utils.showToast
import com.prologic.strains.viewmodel.MyWebViewModel
import kotlinx.android.synthetic.main.my_web_view.*


class MyWebView : AppCompatActivity() {
    lateinit var viewModel: MyWebViewModel
    lateinit var binding: MyWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.my_web_view)
        viewModel = ViewModelProvider(this).get(MyWebViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        swipeRefreshLayout.setOnRefreshListener { loadWebView() }
        back.setOnClickListener { onBackPressed() }
        if (isNetworkAvailable(this)) {
            loadWebView()
        } else {
            AlertError.show(this, "Internet Connection Error", object : OnDialogCloseListener {
                override fun onClick() {
                    finish()
                }
            })
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
            webView?.destroy()
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        title_name.text = intent?.getStringExtra("title")!!
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowContentAccess = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = false
        webSettings.loadWithOverviewMode = true
        webSettings.setAppCacheEnabled(true)
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = WebChromeClient()
        val url = intent?.getStringExtra("url")!!
        webView.loadUrl(url)

    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("http")) {
                view.loadUrl(url)
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            return true
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showToast("Got Error! ${error?.description.toString()}")
            } else {
                showToast("Got Error! ${error?.toString()}")
            }

        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            swipeRefreshLayout.isRefreshing = true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            swipeRefreshLayout.isRefreshing = false
        }
    }


}