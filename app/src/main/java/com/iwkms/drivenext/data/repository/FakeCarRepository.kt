package com.iwkms.drivenext.data.repository

import com.iwkms.drivenext.domain.model.Car
import com.iwkms.drivenext.domain.repository.CarRepository

class FakeCarRepository : CarRepository {

    private val cars = listOf(
        Car(1, "", "S 500 Sedan", "Mercedes-Benz", "2 500 RUB", "AT", "Petrol"),
        Car(2, "", "Model 3", "Tesla", "3 000 RUB", "AT", "Electric"),
        Car(3, "", "Camry", "Toyota", "2 200 RUB", "AT", "Petrol"),
        Car(4, "", "X5", "BMW", "3 500 RUB", "AT", "Diesel")
    )

    override fun getCars(): List<Car> = cars

    override fun searchCars(query: String): List<Car> =
        cars.filter { car ->
            car.brand.contains(query, ignoreCase = true) ||
                car.model.contains(query, ignoreCase = true)
        }

    override fun getCarById(id: Int): Car? = cars.firstOrNull { it.id == id }
}
