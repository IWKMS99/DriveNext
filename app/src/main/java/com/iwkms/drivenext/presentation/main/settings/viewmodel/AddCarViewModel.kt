package com.iwkms.drivenext.presentation.main.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwkms.drivenext.data.repository.FakeCarRepository
import com.iwkms.drivenext.domain.model.Car
import java.util.Random

class AddCarViewModel : ViewModel() {

    val address = MutableLiveData("")

    val year = MutableLiveData("")
    val brand = MutableLiveData("")
    val model = MutableLiveData("")
    val transmission = MutableLiveData("")
    val mileage = MutableLiveData("")
    val description = MutableLiveData("")

    private val _photos = MutableLiveData<List<String>>(emptyList())
    val photos: LiveData<List<String>> get() = _photos

    private val carRepository = FakeCarRepository()

    fun addPhoto(uri: String) {
        val currentList = _photos.value.orEmpty().toMutableList()
        if (currentList.size < 5) {
            currentList.add(uri)
            _photos.value = currentList
        }
    }

    fun isStep1Valid(): Boolean = !address.value.isNullOrBlank()

    fun isStep2Valid(): Boolean {
        return !year.value.isNullOrBlank() &&
                !brand.value.isNullOrBlank() &&
                !model.value.isNullOrBlank() &&
                !transmission.value.isNullOrBlank() &&
                !mileage.value.isNullOrBlank() &&
                !description.value.isNullOrBlank()
    }

    fun saveCar() {
        val newCar = Car(
            id = Random().nextInt(10000) + 1000,
            brand = brand.value!!.trim(),
            model = model.value!!.trim(),
            pricePerDay = 3500,
            transmission = transmission.value!!,
            fuelType = "Бензин",
            description = description.value!!.trim(),
            address = address.value!!.trim(),
            rating = 5.0,
            reviewCount = 0,
            mainImage = _photos.value?.firstOrNull() ?: "",
            images = _photos.value.orEmpty(),
            isFavorite = false
        )
        carRepository.addCar(newCar)
    }
}