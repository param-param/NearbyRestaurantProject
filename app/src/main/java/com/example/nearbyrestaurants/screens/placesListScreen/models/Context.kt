package com.example.nearbyrestaurants.screens.placesListScreen.models

data class Context(
    val geo_bounds: GeoBounds
)

data class GeoBounds(
    val circle: Circle
)

data class Circle(
    val center: Center,
    val radius: Int
)

data class Center(
    val latitude: Double,
    val longitude: Double
)