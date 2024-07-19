package com.example.myapplication.ui.data.model

import java.net.URI

data class Product(
    val name: String,
    val price: Int?,
    val image: String?,
    val size: String,
    val materials_ko: String?,
    val nutrients_ko: NutrientsKo,
    val source_of_manufacture: String?,
    val caution: String?,
    val allergens: List<String>
) {

}

data class NutrientsKo(
    val serving_size: String,
    val calories: String,
    val detail: Map<String, NutrientDetail>
)

data class NutrientDetail(
    val amount: String,
    val percent: String
)
