package com.example.hmcodetest.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search-services/v1/{country}_{language}/search/resultpage")
    suspend fun searchProducts(
        @Path("country") country: String = "sv",
        @Path("language") language: String = "se",
        @Query("touchPoint") touchPoint: String = "android",
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): SearchResponseDto
}