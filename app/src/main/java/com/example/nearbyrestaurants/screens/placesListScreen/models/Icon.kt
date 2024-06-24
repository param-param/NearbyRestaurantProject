package com.learning.mvvmSample.xyzFeatureScreens.models

data class Icon(
    val prefix: String,
    val suffix: String
) {

    fun getIcon(): String = try {
        if (prefix.isNullOrEmpty() || suffix.isNullOrEmpty()) ""
        else  {
            prefix+"64"+suffix
        }
    } catch (e: IndexOutOfBoundsException) {
        e.printStackTrace()
        ""
    }

}