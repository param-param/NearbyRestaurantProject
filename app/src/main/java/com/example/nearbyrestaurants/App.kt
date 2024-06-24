package com.example.nearbyrestaurants

import android.app.Application
import com.demo.moviesapp.di.AppModule
import com.example.nearbyrestaurants.di.ApplicationComponent
import com.example.nearbyrestaurants.di.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }


}