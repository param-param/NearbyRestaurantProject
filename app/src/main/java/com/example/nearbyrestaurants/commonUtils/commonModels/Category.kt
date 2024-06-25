package com.example.nearbyrestaurants.commonUtils.commonModels

import com.example.nearbyrestaurants.screens.placesListScreen.models.Icon

data class Category(
    val icon: Icon,
    val id: Int,
    val name: String,
    val plural_name: String,
    val short_name: String
)