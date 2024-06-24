package com.example.nearbyrestaurants.screens.placesListScreen.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placesListScreen.repository.PlacesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlacesListActivityViewModel (private val repository: PlacesListRepository) : ViewModel() {

    private val _placesListLiveData = MutableLiveData<ResponseType<List<Result>>>()

    val placesList: LiveData<ResponseType<List<Result>>>
        get() = _placesListLiveData

    fun getPlacesList(latLong: String, categories: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlacesList(latLong, categories)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getPlacesList(latLong, categories)
            _placesListLiveData.postValue(result)
        }
    }

}