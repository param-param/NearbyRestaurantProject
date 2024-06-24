package com.example.nearbyrestaurants.screens.placeDetailsScreen.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.Utils
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placeDetailsScreen.models.PlacePhotosResponseItem
import javax.inject.Inject

class PlaceDetailsRepository @Inject constructor(
    private val apiService: ApiService,
    private val applicationContext: Context
) {

    private val _placeDetailResponseLiveData = MutableLiveData<ResponseType<Result>>()
    val placeDetails: LiveData<ResponseType<Result>>
        get() = _placeDetailResponseLiveData

    private val _placePhotosResponseLiveData = MutableLiveData<ResponseType<List<PlacePhotosResponseItem>>>()
    val placePhotos: LiveData<ResponseType<List<PlacePhotosResponseItem>>>
        get() = _placePhotosResponseLiveData


    suspend fun getPlaceDetails(fsqId: String) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val response = apiService.getPlaceDetail(fsqId)
                if (response.body() != null) {
                    _placeDetailResponseLiveData.postValue(ResponseType.Success(response.body()))
                } else {
                    _placeDetailResponseLiveData.postValue(ResponseType.Error(response.message()))
                }
            } catch (exception: Exception) {
                _placeDetailResponseLiveData.postValue(ResponseType.Error(exception.message.toString()))
            }
        } else {
            _placeDetailResponseLiveData.postValue(ResponseType.Error("NO_INTERNET_CONNECTION"))
        }
    }

    suspend fun getPlacePhotos(fsqId: String) {

        if (Utils.isInternetAvailable(applicationContext)) {
            try {
                val response = apiService.getPlacePhoto(fsqId)
                if (response.body() != null) {
                    _placePhotosResponseLiveData.postValue(ResponseType.Success(response.body()))
                } else {
                    _placePhotosResponseLiveData.postValue(ResponseType.Error(response.message()))
                }
            } catch (exception: Exception) {
                _placePhotosResponseLiveData.postValue(ResponseType.Error(exception.message.toString()))
            }
        } else {
            _placePhotosResponseLiveData.postValue(ResponseType.Error("NO_INTERNET_CONNECTION"))
        }
    }

}

