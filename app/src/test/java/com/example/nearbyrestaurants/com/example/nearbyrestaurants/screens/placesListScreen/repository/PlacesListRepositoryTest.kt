package com.example.nearbyrestaurants.com.example.nearbyrestaurants.screens.placesListScreen.repository

import androidx.test.core.app.ApplicationProvider
import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.com.example.nearbyrestaurants.Utility
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placesListScreen.repository.PlacesListRepository
import com.learning.mvvmSample.xyzFeatureScreens.models.PlacesSearchResponse
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

internal class PlacesListRepositoryTest {

    @Mock
    lateinit var apiService: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetMovies_expectedError() = runTest {
        Mockito.`when`(apiService.getPlacesSearch("30.71,76.71", 13199)).thenReturn(
            Response.error(400, "invalid categoryId(s) provided : ; see https://docs.foursquare.com/docs/categories".toResponseBody()))

        val placeListRepository = PlacesListRepository(apiService, ApplicationProvider.getApplicationContext())
        val result = placeListRepository.getPlacesList("30.71,76.71", 13199)
        Assert.assertEquals(true, result is ResponseType.Error<*>)
        Assert.assertEquals("Something went wrong", result.error)
    }

}