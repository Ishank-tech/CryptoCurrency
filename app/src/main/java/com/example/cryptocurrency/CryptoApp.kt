package com.example.cryptocurrency

import android.app.Application
import com.example.cryptocurrency.api.CryptoService
import com.example.cryptocurrency.api.RetrofitHelper
import com.example.cryptocurrency.repository.CryptoRepository
import com.example.cryptocurrency.utils.SharedPrefs

class CryptoApp: Application() {

    lateinit var cryptoRepository: CryptoRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }
    private fun initialize() {
        SharedPrefs.init(this)
        val cryptoService = RetrofitHelper.getInstance().create(CryptoService::class.java)
        cryptoRepository = CryptoRepository(cryptoService)
    }
}