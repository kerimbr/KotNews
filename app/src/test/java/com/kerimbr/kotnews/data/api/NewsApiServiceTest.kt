package com.kerimbr.kotnews.data.api

import com.google.common.truth.Truth.assertThat
import com.kerimbr.kotnews.data.remote.api.NewsAPIService
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NewsApiServiceTest {

    private lateinit var newsApiService: NewsAPIService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        newsApiService = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }


    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream?.source()?.buffer()
        val mockResponse = MockResponse()
        source?.let { mockResponse.setBody(it.readString(Charsets.UTF_8)) }
        server.enqueue(mockResponse)
    }


    @Test
    fun getNewsHeadlines_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("news_fake_response.json")
            val responseBody = newsApiService.getNewsHeadlines("us", 1).body()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
        }
    }



    @After
    fun tearDown() {
        server.shutdown()
    }

}