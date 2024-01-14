package com.example.cryptocurrency.repository

import com.example.cryptocurrency.api.CryptoService
import com.example.cryptocurrency.models.ListResponse
import com.example.cryptocurrency.models.LiveResponse
import com.example.cryptocurrency.utils.NetworkResult
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CryptoRepositoryTest {
    private lateinit var cryptoRepository: CryptoRepository

    @Mock
    private lateinit var cryptoService: CryptoService

    private val testDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cryptoRepository = CryptoRepository(cryptoService)
    }

    @Test
    fun getCurrencies_returns_success_result() = runTest {
        // Arrange
        val mockDetails = ListResponse(emptyMap())
        val mockRates = LiveResponse(emptyMap())

        `when`(cryptoService.getCryptoList(Mockito.anyString())).thenReturn(Response.success(mockDetails))
        `when`(cryptoService.getExchangeRates(Mockito.anyString())).thenReturn(Response.success(mockRates))

        // Act
        val result = cryptoRepository.getCurrencies()

        // Assert
        assert(result is NetworkResult.Success)
        assert((result as NetworkResult.Success).data?.size == mockDetails.crypto.size)
    }

    @Test
    fun getCurrencies_handles_error_response_from_details_API() = runTest {
        // Arrange
        `when`(cryptoService.getCryptoList(Mockito.anyString())).thenReturn(Response.error(400, ResponseBody.create(null, "Error response")))

        // Act
        val result = cryptoRepository.getCurrencies()

        // Assert
        assert(result is NetworkResult.Error)
    }

    @Test
    fun getCurrencies_handles_error_response_from_rates_API() = runTest {
        // Arrange
        `when`(cryptoService.getExchangeRates(Mockito.anyString())).thenReturn(Response.error(400, ResponseBody.create(null, "Error response")))

        // Act
        val result = cryptoRepository.getCurrencies()

        // Assert
        assert(result is NetworkResult.Error)
    }

    @After
    fun tearDown() {
        testCoroutineScope.cleanupTestCoroutines()
    }
}