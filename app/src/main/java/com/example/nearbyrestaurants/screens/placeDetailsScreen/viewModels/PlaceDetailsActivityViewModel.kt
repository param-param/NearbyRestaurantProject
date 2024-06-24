package com.example.nearbyrestaurants.screens.placeDetailsScreen.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placeDetailsScreen.models.PlacePhotosResponseItem
import com.example.nearbyrestaurants.screens.placeDetailsScreen.repository.PlaceDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceDetailsActivityViewModel(private val repository: PlaceDetailsRepository) : ViewModel() {

    val placeDetails: LiveData<ResponseType<Result>>
        get() = repository.placeDetails

    val placePhotos: LiveData<ResponseType<List<PlacePhotosResponseItem>>>
        get() = repository.placePhotos

    fun getPlaceDetails(fsqId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlaceDetails(fsqId)
        }
    }

    fun getPlacePhotos(fsqId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlacePhotos(fsqId)
        }
    }

}