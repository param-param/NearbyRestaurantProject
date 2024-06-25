package com.example.nearbyrestaurants.com.example.nearbyrestaurants.screens.placesListScreen.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.com.example.nearbyrestaurants.Utility
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.screens.placesListScreen.models.PlacesSearchResponse
import com.example.nearbyrestaurants.screens.placesListScreen.repository.PlacesListRepository
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response


@ExperimentalCoroutinesApi
internal class PlacesListRepositoryTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var mockContext: Context

    @Mock
    lateinit var mockConnectivityManager: ConnectivityManager

    @Mock
    lateinit var mockNetworkInfo: NetworkInfo

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)
        Mockito.`when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
    }
    @Test
    fun testGetPlacesSearch_expectedMovieList() = runTest {
        Mockito.`when`(mockNetworkInfo.isConnected).thenReturn(true)

        val content = Utility.readFileResource("/sample_response.json")
        val placesSearchResponse: PlacesSearchResponse = Gson().fromJson(content, PlacesSearchResponse::class.java)
        val placesSearchList = ArrayList<Result>()
        placesSearchList.addAll(placesSearchResponse.results)

        Mockito.`when`(apiService.getPlacesSearch("30.71,76.71", 13199)).thenReturn(
            Response.success(placesSearchResponse)
        )

        val placeListRepository = PlacesListRepository(apiService, mockContext)
        val placesSearchRes = placeListRepository.getPlacesList("30.71,76.71", 13199)
        Assert.assertEquals(true, placesSearchRes is ResponseType.Success)
        Assert.assertEquals(10, placesSearchRes.data!!.size)
        Assert.assertEquals("Ghazal Restaurant", placesSearchRes.data!![1].name)
    }

    @Test
    fun testGetPlacesSearch_expectedError() = runTest {
        Mockito.`when`(mockNetworkInfo.isConnected).thenReturn(true)

        Mockito.`when`(apiService.getPlacesSearch("30.71,76.71", 13199)).thenReturn(
            Response.error(400, "invalid categoryId(s) provided : ; see https://docs.foursquare.com/docs/categories".toResponseBody())
        )

        val placeListRepository = PlacesListRepository(apiService, mockContext)
        val result = placeListRepository.getPlacesList("30.71,76.71", 13199)
        Assert.assertEquals(true, result is ResponseType.Error<*>)
    }

}