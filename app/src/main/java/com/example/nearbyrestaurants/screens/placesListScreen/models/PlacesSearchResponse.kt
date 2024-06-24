package com.learning.mvvmSample.xyzFeatureScreens.models

import com.example.nearbyrestaurants.commonUtils.commonModels.Result

data class PlacesSearchResponse(
    val context: Context,
    val results: List<Result>
)
