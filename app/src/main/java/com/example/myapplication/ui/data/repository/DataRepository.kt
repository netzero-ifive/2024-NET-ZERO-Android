package com.example.myapplication.ui.data.repository

import com.example.myapplication.ui.data.model.BottleReturn
import com.example.myapplication.ui.data.model.Kiosk
import com.example.myapplication.ui.data.model.Product
import com.example.myapplication.ui.data.network.ApiService

class DataRepository(private val apiService: ApiService) {
    suspend fun getAllergensList(): List<String> = apiService.getAllergensList().allergenList
    suspend fun getProduct(id: Int): Product = apiService.getProduct(id)
    suspend fun getBottleReturns(latitude: Double, longitude: Double): List<BottleReturn> {
        return apiService.getBottleReturns(latitude, longitude)
    }
    suspend fun getKiosks(latitude: Double, longitude: Double): List<Kiosk> {
        return apiService.getKiosks(latitude, longitude)
    }
}
