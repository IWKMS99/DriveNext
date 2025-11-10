package com.iwkms.drivenext.presentation.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.ItemCarCardBinding
import com.iwkms.drivenext.domain.model.Car

class CarAdapter(
    private val onBookClick: (Car) -> Unit,
    private val onDetailsClick: (Car) -> Unit
) : ListAdapter<Car, CarAdapter.CarViewHolder>(CarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarViewHolder(private val binding: ItemCarCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car) {
            binding.tvCarModel.text = car.model
            binding.tvCarBrand.text = car.brand
            binding.tvCarPrice.text = binding.root.context.getString(
                R.string.car_card_price_per_day,
                car.pricePerDay
            )
            binding.tvTransmission.text = car.transmission
            binding.tvFuelType.text = car.fuelType

            binding.btnBook.setOnClickListener { onBookClick(car) }
            binding.btnDetails.setOnClickListener { onDetailsClick(car) }
        }
    }

    class CarDiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Car, newItem: Car) = oldItem == newItem
    }
}
