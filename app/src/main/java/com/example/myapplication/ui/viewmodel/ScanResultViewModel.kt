package com.example.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.data.model.Product
import com.example.myapplication.ui.data.network.RetrofitInstance
import com.example.myapplication.ui.data.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScanResultViewModel(private val repository: DataRepository): ViewModel(){

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _product.value = repository.getProduct(id)
            } catch (e: Exception) {
                Log.d("ReZero","$e")

                _error.value = "Failed to load product: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}


class ScanResultViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanResultViewModel::class.java)) {
            val apiService = RetrofitInstance.api
            val repository = DataRepository(apiService)
            @Suppress("UNCHECKED_CAST")
            return ScanResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
