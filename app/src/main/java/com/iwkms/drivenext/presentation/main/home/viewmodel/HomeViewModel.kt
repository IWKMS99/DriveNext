package com.iwkms.drivenext.presentation.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwkms.drivenext.domain.model.Car

class HomeViewModel : ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> get() = _cars

    init {
        loadCars()
    }

    private fun loadCars() {
        // TODO: Заменить на загрузку данных из репозитория
        val staticCars = listOf(
            Car(1, "", "S 500 Sedan", "Mercedes-Benz", "2500Р", "А/Т", "Бензин"),
            Car(2, "", "Model 3", "Tesla", "3000Р", "А/Т", "Электро"),
            Car(3, "", "Camry", "Toyota", "2200Р", "А/Т", "Бензин")
        )
        _cars.value = staticCars
    }
}