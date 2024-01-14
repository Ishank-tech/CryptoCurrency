package com.example.cryptocurrency.repository

import com.example.cryptocurrency.BuildConfig.API_KEY
import com.example.cryptocurrency.api.CryptoService
import com.example.cryptocurrency.models.Crypto
import com.example.cryptocurrency.models.Currency
import com.example.cryptocurrency.utils.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

class CryptoRepository(private val cryptoService: CryptoService) {

    suspend fun getCurrencies(): NetworkResult<List<Currency>> {
        return try {
            val details = CoroutineScope(Dispatchers.IO).async { cryptoService.getCryptoList(API_KEY) }.await()
            val rates = CoroutineScope(Dispatchers.IO).async { cryptoService.getExchangeRates(API_KEY) }.await()

            if (details.isSuccessful && rates.isSuccessful) {
                val detailBody = details.body()
                val rateBody = rates.body()
                if (detailBody != null && rateBody != null) {
                    val currencies = createCurrencies(detailBody.crypto, rateBody.rates)
                    NetworkResult.Success(currencies)
                } else {
                    val errorMessage = if (detailBody == null) "Not able to fetch details"
                    else if (rateBody == null) "Not able to fetch exchange rates"
                    else "Something went wrong"
                    NetworkResult.Error(errorMessage)
                }
            } else {
                val errorBody = details.errorBody() ?: rates.errorBody()
                val errorMessage = getErrorMessageFromErrorBody(errorBody)
                NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            NetworkResult.Error("Exception: ${e.message}")
        }
    }

    private fun createCurrencies(
        cryptoMap: Map<String, Crypto>,
        rates: Map<String, Double>
    ) = cryptoMap.entries.map { (symbol, cryptoCurrency) ->
        val rate = rates[symbol] ?: 0.0
        Currency(symbol, cryptoCurrency.name, cryptoCurrency.iconUrl, rate)
    }

    private fun getErrorMessageFromErrorBody(errorBody: ResponseBody?): String {
        if (errorBody != null) {
            try {
                val errorObj = JSONObject(errorBody.charStream().readText())
                return errorObj.getString("message")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return "Something went wrong"
    }

}