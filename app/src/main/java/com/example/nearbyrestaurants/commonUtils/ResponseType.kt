package com.example.nearbyrestaurants.commonUtils

sealed class ResponseType<T>(val data: T? = null, val error: String? = null) {

    class Loading<T> : ResponseType<T>()
    class Success<T>(data: T? = null) : ResponseType<T>(data = data)
    class Error<T>(error: String? = null) : ResponseType<T>(error = error)

}