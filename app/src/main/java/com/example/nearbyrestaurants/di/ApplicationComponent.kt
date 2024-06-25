package com.example.nearbyrestaurants.di

import com.example.nearbyrestaurants.screens.placeDetailsScreen.PlaceDetailsActivity
import com.example.nearbyrestaurants.screens.placesListScreen.PlacesListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface ApplicationComponent {

    fun inject(placesListActivity: PlacesListActivity)
    fun inject(placeDetailsActivity: PlaceDetailsActivity)


}