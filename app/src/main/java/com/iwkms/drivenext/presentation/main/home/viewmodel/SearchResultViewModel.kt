package com.iwkms.drivenext.presentation.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwkms.drivenext.domain.model.Car

class SearchResultViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<Car>>()
    val searchResults: LiveData<List<Car>> get() = _searchResults

    private val allCars = listOf(
        Car(1, "", "S 500 Sedan", "Mercedes-Benz", "2500Р", "А/Т", "Бензин"),
        Car(2, "", "Model 3", "Tesla", "3000Р", "А/Т", "Электро"),
        Car(3, "", "Camry", "Toyota", "2200Р", "А/Т", "Бензин"),
        Car(4, "", "X5", "BMW", "3500Р", "А/Т", "Дизель")
    )

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        val results = allCars.filter { car ->
            car.brand.contains(query, ignoreCase = true) || car.model.contains(query, ignoreCase = true)
        }
        _searchResults.value = results
    }
}