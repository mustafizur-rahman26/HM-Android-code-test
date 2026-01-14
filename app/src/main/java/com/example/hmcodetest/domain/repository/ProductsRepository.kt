package com.example.hmcodetest.domain.repository

import com.example.hmcodetest.domain.model.PaginatedProducts
import com.example.hmcodetest.util.Async

interface ProductsRepository {
    suspend fun getProducts(
        page: Int = 1
    ): Async<PaginatedProducts>
}