package com.prologic.laughinggoat.network
import com.prologic.laughinggoat.BuildConfig
import com.prologic.laughinggoat.utils.UrlValue

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("consumer_key", UrlValue.consumer_key)
            .addQueryParameter("consumer_secret", UrlValue.consumer_secret)
            .build()
        val requestBuilder = original.newBuilder()
        requestBuilder.addHeader("app-version", BuildConfig.VERSION_NAME)
        requestBuilder.url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}