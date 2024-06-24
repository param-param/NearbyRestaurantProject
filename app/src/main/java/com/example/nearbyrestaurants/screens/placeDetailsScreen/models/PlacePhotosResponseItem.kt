package com.example.nearbyrestaurants.screens.placeDetailsScreen.models

data class PlacePhotosResponseItem(
    val created_at: String,
    val height: Int,
    val id: String,
    val prefix: String,
    val suffix: String,
    val width: Int
) {

    fun getPhoto(): String = try {
        if (prefix.isNullOrEmpty() || suffix.isNullOrEmpty()) ""
        else  {
            prefix+"1024"+suffix
        }
    } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
        ""
    }
}