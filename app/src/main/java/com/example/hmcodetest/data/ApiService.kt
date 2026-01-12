package com.example.hmcodetest.data

import retrofit2.http.GET

interface ApiService {

    @GET("xyz")
    suspend fun getProducts(): Product
}

data class Product (
    val name: String
)