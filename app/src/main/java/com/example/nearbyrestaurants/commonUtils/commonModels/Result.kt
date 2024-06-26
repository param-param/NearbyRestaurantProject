package com.example.nearbyrestaurants.commonUtils.commonModels

import com.example.nearbyrestaurants.screens.placesListScreen.models.Location

data class Result(
    val categories: List<Category>,
    val closed_bucket: String,
    val fsq_id: String,
    val link: String,
    val location: Location,
    val name: String,
)