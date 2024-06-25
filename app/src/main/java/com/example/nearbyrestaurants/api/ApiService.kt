package com.example.nearbyrestaurants.api

import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placeDetailsScreen.models.PlacePhotosResponseItem
import com.example.nearbyrestaurants.screens.placesListScreen.models.PlacesSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("places/search")
    suspend fun getPlacesSearch(
        @Query("ll") latLong: String,
        @Query("categories") categories: Int
    ): Response<PlacesSearchResponse>


    @GET("places/{fsq_id}")
    suspend fun getPlaceDetail(
        @Path("fsq_id") fsqId: String
    ): Response<Result>

    @GET("places/{fsq_id}/photos")
    suspend fun getPlacePhoto(
        @Path("fsq_id") fsqId: String
    ): Response<List<PlacePhotosResponseItem>>

}