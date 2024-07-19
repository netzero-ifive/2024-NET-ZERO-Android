package com.example.myapplication.ui.data.model

data class Kiosk(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val created_at: String,
    val updated_at: String,
    val distance: Double,
    val formatted_distance: String,
    val products: List<Product>
)
