package com.example.nearbyrestaurants.screens.placeDetailsScreen.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nearbyrestaurants.screens.placeDetailsScreen.repository.PlaceDetailsRepository
import javax.inject.Inject

class PlaceDetailsActivityViewModelFactory @Inject constructor(private val repository: PlaceDetailsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaceDetailsActivityViewModel(repository) as T
    }

}
