package com.example.nearbyrestaurants.commonUtils.commonModels

import com.learning.mvvmSample.xyzFeatureScreens.models.Icon

data class Category(
    val icon: Icon,
    val id: Int,
    val name: String,
    val plural_name: String,
    val short_name: String
)