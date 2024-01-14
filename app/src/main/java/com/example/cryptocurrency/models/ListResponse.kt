package com.example.cryptocurrency.models

import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("crypto")
    val crypto: Map<String, Crypto>
)