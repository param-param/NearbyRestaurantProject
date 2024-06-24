package com.example.nearbyrestaurants.com.example.nearbyrestaurants

import java.io.InputStreamReader

object Utility {

    fun readFileResource(filename: String) : String{
        val inputStream = Utility::class.java.getResourceAsStream(filename)
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it)
        }
        return builder.toString()
    }
}