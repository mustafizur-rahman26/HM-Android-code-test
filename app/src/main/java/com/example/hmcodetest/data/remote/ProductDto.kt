package com.example.hmcodetest.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("searchHits")
    val searchHits: SearchHitsDto,

    @SerialName("pagination")
    val pagination: PaginationDto
)

@Serializable
data class SearchHitsDto(
    @SerialName("productList")
    val productList: List<ProductDto> = emptyList()
)

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String,

    @SerialName("productName")
    val productName: String,

    @SerialName("brandName")
    val brandName: String,

    @SerialName("prices")
    val prices: List<PriceDto>,

    @SerialName("productImage")
    val productImage: String,
    
    @SerialName("swatches")
    val swatches: List<SwatchDto>
)

@Serializable
data class PriceDto(
    @SerialName("formattedPrice")
    val formattedPrice: String
)

@Serializable
data class SwatchDto(
    @SerialName("colorCode")
    val colorCode: String,
)


@Serializable
data class PaginationDto(
    @SerialName("currentPage")
    val currentPage: Int,

    @SerialName("nextPageNum")
    val nextPageNum: Int,
    
    @SerialName("totalPages")
    val totalPages: Int
)
