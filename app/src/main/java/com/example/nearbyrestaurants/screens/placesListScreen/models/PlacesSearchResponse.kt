package com.example.nearbyrestaurants.screens.placesListScreen.models

import com.example.nearbyrestaurants.commonUtils.commonModels.Result

data class PlacesSearchResponse(
    val context: Context,
    val results: List<Result>
)