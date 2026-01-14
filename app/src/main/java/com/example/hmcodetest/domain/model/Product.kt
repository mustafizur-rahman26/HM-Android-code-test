package com.example.hmcodetest.domain.model

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val price: String?,
    val thumbnail: String,
    val swatches: List<Swatch>
)

data class Swatch(
    val colorCode: String
)


