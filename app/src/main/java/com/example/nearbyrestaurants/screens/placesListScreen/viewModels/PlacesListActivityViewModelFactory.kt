package com.example.nearbyrestaurants.screens.placesListScreen.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nearbyrestaurants.screens.placesListScreen.repository.PlacesListRepository
import javax.inject.Inject

class PlacesListActivityViewModelFactory @Inject constructor(private val repository: PlacesListRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlacesListActivityViewModel(repository) as T
    }

}
