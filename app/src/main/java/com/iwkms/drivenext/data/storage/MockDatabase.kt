package com.iwkms.drivenext.data.storage

import com.iwkms.drivenext.domain.model.Booking
import com.iwkms.drivenext.domain.model.BookingStatus
import com.iwkms.drivenext.domain.model.Car
import java.util.Calendar

object MockDatabase {

    val cars: MutableList<Car> = mutableListOf(
        Car(
            id = 1,
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            pricePerDay = 2500,
            transmission = "А/Т",
            fuelType = "Бензин",
            description = "Роскошный седан S-класса с передовыми технологиями комфорта и безопасности. Идеально подходит для деловых поездок и торжественных мероприятий.",
            address = "Авиамоторная ул., 8, стр. 2",
            rating = 4.9,
            reviewCount = 124,
            mainImage = "",
            images = emptyList(),
            isFavorite = false
        ),
        Car(
            id = 2,
            brand = "Tesla",
            model = "Model 3 2019",
            pricePerDay = 3000,
            transmission = "А/Т",
            fuelType = "Электро",
            description = "Tesla Model 3 оснащена электрическим двигателем, который обеспечивает 460 лошадиных сил. Полный привод. Современный автопилот.",
            address = "Авиамоторная ул., 8, стр. 2",
            rating = 4.8,
            reviewCount = 89,
            mainImage = "",
            images = emptyList(),
            isFavorite = true
        ),
        Car(
            id = 3,
            brand = "Audi",
            model = "A4",
            pricePerDay = 2100,
            transmission = "А/Т",
            fuelType = "Дизель",
            description = "Классический немецкий седан. Комфорт, динамика и стиль в одном флаконе. Экономичный дизельный двигатель.",
            address = "Ленинский проспект, 32",
            rating = 4.7,
            reviewCount = 56,
            mainImage = "",
            images = emptyList(),
            isFavorite = false
        )
    )

    val bookings: MutableList<Booking> = mutableListOf(
        Booking(
            id = "874659",
            carId = 3,
            carName = "Audi A4",
            carImage = "",
            startDate = getTimestamp(-5),
            endDate = getTimestamp(-2),
            totalPrice = 6300,
            status = BookingStatus.COMPLETED
        )
    )

    private fun getTimestamp(daysOffset: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        return calendar.timeInMillis
    }
}