package com.example.myapplication.ui.data.network

import com.example.myapplication.ui.data.model.AllergenResponse
import com.example.myapplication.ui.data.model.BottleReturn
import com.example.myapplication.ui.data.model.Kiosk
import com.example.myapplication.ui.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("allergens_list/")
    suspend fun getAllergensList(): AllergenResponse

    @GET("product/{id}/")
    suspend fun getProduct(@Path("id") id: Int): Product

    @GET("bottle-return/")
    suspend fun getBottleReturns(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): List<BottleReturn>

    @GET("kiosks/")
    suspend fun getKiosks(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): List<Kiosk>
}


