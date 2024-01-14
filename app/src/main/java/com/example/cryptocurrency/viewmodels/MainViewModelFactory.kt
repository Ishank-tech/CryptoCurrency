package com.example.cryptocurrency.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency.repository.CryptoRepository
import com.example.cryptocurrency.utils.SharedPrefs

class MainViewModelFactory(private val cryptoRepository: CryptoRepository, private val sharedPref: SharedPrefs): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(cryptoRepository, sharedPref) as T
    }
}