package com.example.cryptocurrency.models

import com.google.gson.annotations.SerializedName

data class Crypto(
    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("name_full")
    val name: String,

    @SerializedName("icon_url")
    val iconUrl: String
)
