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

class SearchResultViewModel(
    private val carRepository: CarRepository = FakeCarRepository()
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Car>>()
    val searchResults: LiveData<List<Car>> get() = _searchResults

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorResId = MutableLiveData<Int?>()
    val errorResId: LiveData<Int?> get() = _errorResId

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            _errorResId.value = null
            _isLoading.value = false
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorResId.value = null
            delay(400)
            runCatching { carRepository.searchCars(query) }
                .onSuccess { _searchResults.value = it }
                .onFailure { _errorResId.value = R.string.search_loading_error }
            _isLoading.value = false
        }
    }
}
