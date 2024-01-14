package com.example.cryptocurrency.models

import com.google.gson.annotations.SerializedName

data class LiveResponse(
    @SerializedName("rates")
    val rates: Map<String, Double>
)
