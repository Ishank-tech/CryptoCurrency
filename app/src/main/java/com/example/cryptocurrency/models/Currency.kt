package com.example.cryptocurrency.models

data class Currency(
    val symbol: String,
    val fullName: String,
    val iconUrl: String,
    val exchangeRates: Double
)