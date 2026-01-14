package com.example.hmcodetest.data.mapper

import com.example.hmcodetest.data.remote.ProductDto
import com.example.hmcodetest.data.remote.SearchResponseDto
import com.example.hmcodetest.domain.model.PaginatedProducts
import com.example.hmcodetest.domain.model.Product
import com.example.hmcodetest.domain.model.Swatch

fun SearchResponseDto.toPaginatedProducts(): PaginatedProducts = PaginatedProducts(
    products = this.searchHits.productList.map { it.toProduct() },
    currentPage = this.pagination.currentPage,
    totalPages = this.pagination.totalPages,
)


fun ProductDto.toProduct(): Product = Product(
    id = id,
    name = productName,
    brand = brandName,
    price = prices.firstOrNull()?.formattedPrice,
    thumbnail = productImage,
    swatches = swatches.map { Swatch(it.colorCode) }
)

