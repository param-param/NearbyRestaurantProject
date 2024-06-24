package com.example.nearbyrestaurants.screens.placesListScreen.repository

import android.content.Context
import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.Utils
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import javax.inject.Inject

class PlacesListRepository @Inject constructor(
    private val apiService: ApiService,
    private val applicationContext: Context
) {

//    private val _placesSearchResponseLiveData = MutableLiveData<ResponseType<List<Result>>>()
//
//    val places: LiveData<ResponseType<List<Result>>>
//        get() = _placesSearchResponseLiveData

    suspend fun getPlacesList(latLong: String, categories: Int) : ResponseType<List<Result>>{

        return if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val response = apiService.getPlacesSearch(latLong, categories)
                if (response.body() != null) {
                    ResponseType.Success(response.body()!!.results)
//                    _placesSearchResponseLiveData.postValue(ResponseType.Success(response.body()!!.results))
                } else {
                    ResponseType.Error(response.message())
//                    _placesSearchResponseLiveData.postValue(ResponseType.Error(response.message()))
                }
            } catch (exception: Exception) {
                ResponseType.Error(exception.message.toString())
//                _placesSearchResponseLiveData.postValue(ResponseType.Error(exception.message.toString()))
            }
        } else {
             ResponseType.Error("NO_INTERNET_CONNECTION")
//            _placesSearchResponseLiveData.postValue(ResponseType.Error("NO_INTERNET_CONNECTION"))
        }
    }

}
