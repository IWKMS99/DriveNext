package com.iwkms.drivenext.presentation.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iwkms.drivenext.R
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.domain.model.Car
import com.iwkms.drivenext.domain.repository.CarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val carRepository: CarRepository = FakeCarRepository()
) : ViewModel() {

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> get() = _cars

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorResId = MutableLiveData<Int?>()
    val errorResId: LiveData<Int?> get() = _errorResId

    init {
        loadCars()
    }

    fun reload() {
        loadCars()
    }

    private fun loadCars() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            delay(400)
            runCatching { carRepository.getCars() }
                .onSuccess { carList ->
                    _cars.value = carList
                }
                .onFailure {
                    _errorResId.value = R.string.home_loading_error
                }
            _isLoading.value = false
        }
    }
}
