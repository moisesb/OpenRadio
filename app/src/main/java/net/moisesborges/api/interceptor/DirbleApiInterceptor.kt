package net.moisesborges.api.interceptor

import android.content.res.Resources
import net.moisesborges.R
import okhttp3.Interceptor
import okhttp3.Response

class DirbleApiInterceptor(
    private val resources: Resources
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val newurl = originalUrl.newBuilder()
            .addQueryParameter("token", resources.getString(R.string.dirble_api_key))
            .build()
        val newRequest = originalRequest.newBuilder()
            .url(newurl)
            .build()
        return chain.proceed(newRequest)
}
}