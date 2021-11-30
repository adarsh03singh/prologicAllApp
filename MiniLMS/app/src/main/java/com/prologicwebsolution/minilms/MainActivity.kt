package com.prologicwebsolution.minilms


import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var progress: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_bar_layout)
        webView = findViewById(R.id.webView)
//        webView.setWebViewClient(MyBrowser())

        val url = "https://minilms.wp.urdemo.website"

        webView.getSettings().setLoadsImagesAutomatically(true)
        webView.getSettings().setJavaScriptEnabled(true)
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webView.loadUrl(url)


        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view.visibility = View.INVISIBLE
                progress.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.visibility =View.VISIBLE
                progress.visibility = View.GONE
            }
        }
    }

}