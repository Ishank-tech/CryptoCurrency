package com.example.cryptocurrency.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cryptocurrency.models.Currency
import com.example.cryptocurrency.repository.CryptoRepository
import com.example.cryptocurrency.utils.NetworkResult
import com.example.cryptocurrency.utils.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Mock
    private lateinit var cryptoRepository: CryptoRepository

    @Mock
    private lateinit var sharedPrefs: SharedPrefs

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun refreshData_updates_currencies() = runTest {
        // Arrange
        val viewModel = MainViewModel(cryptoRepository, sharedPrefs)

        // Mock data for testing
        val mockCurrencies = listOf<Currency>(Currency("USD", "United", "", 1.5))

        // Mock the repository response
        `when`(cryptoRepository.getCurrencies()).thenReturn(NetworkResult.Success(mockCurrencies))

        // Act
        CoroutineScope(testDispatcher).async {
            viewModel.refreshData()
        }.await()

        println("Currencies LiveData: ${viewModel.currencies.getOrAwaitValue()}")
        println("Last Refresh Time LiveData: ${viewModel.lastRefreshTime.getOrAwaitValue()}")

        // Assert
        assert(viewModel.currencies.getOrAwaitValue() is NetworkResult.Success)
        assert((viewModel.currencies.getOrAwaitValue() as? NetworkResult.Success)?.data == mockCurrencies)
    }

    @Test
    fun cleanup() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }

}