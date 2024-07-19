package com.example.myapplication.ui.data.model

import com.google.gson.annotations.SerializedName

data class AllergenResponse(
    @SerializedName("allergen_list") val allergenList: List<String>
)