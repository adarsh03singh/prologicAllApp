package com.prologic.strains.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import androidx.databinding.DataBindingUtil


import android.webkit.WebSettings

import android.webkit.WebChromeClient
import com.prologic.strains.R
import com.prologic.strains.databinding.PostInfoBinding

import com.prologic.strains.utils.shooterFragment
import com.prologic.strains.view.activity.MainActivity
import com.prologic.strains.viewmodel.PostInfoViewModel

import kotlinx.android.synthetic.main.post_info.webView


class PostInfo : Fragment() {
    lateinit var viewModel: PostInfoViewModel
    lateinit var binding: PostInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PostInfoViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.post_info, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader()

        loadWebView()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView() {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowContentAccess = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        //webSettings.textSize = WebSettings.TextSize.NORMAL
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = false
        webSettings.loadWithOverviewMode = true
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = WebChromeClient()
        var contentValue = arguments?.getString("content")!!
        contentValue =
            "<style>img{display: inline;height: auto;max-width: 100%;}</style>$contentValue"
        webView.loadDataWithBaseURL(null, contentValue, "text/html", "UTF-8", null)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            setHeader()
    }

    private fun setHeader() {
        shooterFragment = this
        (activity as MainActivity).setHeader(
            arguments?.getString("title")!!,
            true,
            false,
            true,
            false
        )
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

        }
    }


}