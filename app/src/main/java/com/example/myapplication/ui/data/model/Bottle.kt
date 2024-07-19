package com.example.myapplication.ui.data.model

data class BottleReturn(
    val id: Int,
    val address: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double,
    val formatted_distance: String
)
