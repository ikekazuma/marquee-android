package dev.ikekazuma.marquee.core.network

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Accept", "application/json")
                .build()
        return chain.proceed(request)
    }
}
