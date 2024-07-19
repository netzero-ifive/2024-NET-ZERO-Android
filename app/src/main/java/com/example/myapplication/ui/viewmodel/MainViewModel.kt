package com.example.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.model.BottleReturn
import com.example.myapplication.ui.data.model.Kiosk
import com.example.myapplication.ui.data.model.Product
import com.example.myapplication.ui.data.network.RetrofitInstance
import com.example.myapplication.ui.data.repository.DataRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository): ViewModel() {

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    fun updateCurrentLocation(location: LatLng) {
        _currentLocation.value = location

    }

    private val _allergens = MutableStateFlow<List<String>>(emptyList())
    val allergens: StateFlow<List<String>> = _allergens


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAllergens() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _allergens.value = repository.getAllergensList()
            } catch (e: Exception) {
                Log.d("ReZero","$e")
                _error.value = "Failed to load allergens: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _bottleReturns = MutableStateFlow<List<BottleReturn>>(emptyList())
    val bottleReturns: StateFlow<List<BottleReturn>> = _bottleReturns

    fun fetchBottleReturns(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getBottleReturns(latitude, longitude)
                Log.d("ReZero","$result")
                _bottleReturns.value = result
            } catch (e: Exception) {
                Log.d("ReZero","$e")
                _error.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _kiosks = MutableStateFlow<List<Kiosk>>(emptyList())
    val kiosks: StateFlow<List<Kiosk>> = _kiosks

    fun fetchKiosks(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getKiosks(latitude, longitude)
                _kiosks.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }



}
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val apiService = RetrofitInstance.api
            val repository = DataRepository(apiService)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}