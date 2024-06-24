package com.example.nearbyrestaurants.api

import okhttp3.Interceptor
import okhttp3.Response

// This class is used for adding dynamic apikey value to header for request
class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .header("Authorization", "$apiKey")
            .build()
        return chain.proceed(requestWithHeaders)
    }
}
