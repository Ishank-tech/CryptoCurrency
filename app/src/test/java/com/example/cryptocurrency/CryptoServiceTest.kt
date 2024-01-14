package com.example.cryptocurrency

import com.example.cryptocurrency.api.CryptoService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var cryptoService: CryptoService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        cryptoService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getExchangeRates_should_return_LiveResponse() = runBlocking {
        // Arrange
        val apiKey = "live"
        val response = MockResponse().setResponseCode(200)
            .setBody("{ \"success\": true, \"rates\": { \"USD\": 1.0, \"EUR\": 0.85 } }")
        mockWebServer.enqueue(response)

        // Act
        val result = cryptoService.getExchangeRates(apiKey)

        // Assert
        assert(result.isSuccessful)
        assert(result.body() != null)
        assert(result.body()!!.rates.isNotEmpty())
    }

    @Test
    fun getCryptoList_should_return_ListResponse() = runBlocking {
        // Arrange
        val apiKey = "list"
        val response = MockResponse().setResponseCode(200)
            .setBody("{ \"success\": true, \"cryptoList\": { \"USD\":{\"symbol\":\"USD\", \"name_full\":\"United States Dollar\", \"icon_url\":\"\"} }}")
        mockWebServer.enqueue(response)

        // Act
        val result = cryptoService.getCryptoList(apiKey)
        println(result.body()?.crypto)
        // Assert
        assert(result.isSuccessful)
        assert(result.body() != null)
    }

}