package com.example.hmcodetest.domain.model

data class PaginatedProducts(
    val products: List<Product>,
    val currentPage: Int,
    val nextPage: Int?,
    val totalPages: Int,
) {
    val hasMorePages: Boolean
        get() = currentPage < totalPages
}
