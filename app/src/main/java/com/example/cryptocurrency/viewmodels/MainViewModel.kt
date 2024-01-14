package com.example.cryptocurrency.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.models.Currency
import com.example.cryptocurrency.repository.CryptoRepository
import com.example.cryptocurrency.utils.NetworkResult
import com.example.cryptocurrency.utils.SharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainViewModel(private val cryptoRepository: CryptoRepository, private val sharedPrefs: SharedPrefs): ViewModel(){

    private val _currencies = MutableLiveData<NetworkResult<List<Currency>>>()
    val currencies: LiveData<NetworkResult<List<Currency>>> get() = _currencies

    private var _lastRefreshTime = MutableLiveData<String>()
    val lastRefreshTime: LiveData<String> = _lastRefreshTime

    init {
        _lastRefreshTime.postValue(sharedPrefs.getTime())
        viewModelScope.launch{
            refreshData()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            val currency = CoroutineScope(Dispatchers.IO).async { cryptoRepository.getCurrencies() }.await()
            _currencies.postValue(currency)
            updateLastRefreshTime()
        }
    }

    fun updateLastRefreshTime() {
        val currentTime = System.currentTimeMillis()
        val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .format(currentTime)
        sharedPrefs.saveTime("Last Refresh: $formattedDate")
        _lastRefreshTime.postValue("Last Refresh: $formattedDate")
    }
}