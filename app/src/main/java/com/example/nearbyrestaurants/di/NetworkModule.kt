package com.example.nearbyrestaurants.di

import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.api.AuthInterceptor
import com.example.nearbyrestaurants.commonUtils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(Constants.API_KEY))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}