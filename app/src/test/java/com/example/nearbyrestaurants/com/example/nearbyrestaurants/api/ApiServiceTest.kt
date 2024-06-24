package com.example.nearbyrestaurants.com.example.nearbyrestaurants.api


import com.example.nearbyrestaurants.api.ApiService
import com.example.nearbyrestaurants.com.example.nearbyrestaurants.Utility
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    @Test
    fun testGetPlacesSearch(): Unit = runTest{
        val mockResponse = MockResponse()
        mockResponse.setBody("{}")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getPlacesSearch("30.71,76.71", 13199)
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.body()!!.results.isNullOrEmpty())
    }

    @Test
    fun testGetPlacesSearch_returnPlaces() = runTest{
        val mockResponse = MockResponse()
        val content = Utility.readFileResource("/sample_response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getPlacesSearch("30.71,76.71", 13199)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.body()!!.results.isEmpty())
        Assert.assertEquals(10, response.body()!!.results.size)
    }


    @Test
    fun testGetPlacesSearch_returnError() = runTest{
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        val response = apiService.getPlacesSearch("30.71,76.71", 13199)
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}