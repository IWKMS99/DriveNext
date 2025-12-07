package com.iwkms.drivenext.presentation.main.settings.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwkms.drivenext.R
import com.iwkms.drivenext.databinding.ItemBookingBinding
import com.iwkms.drivenext.domain.model.Booking
import com.iwkms.drivenext.domain.model.BookingStatus
import java.util.Date

class BookingsAdapter(
    private val onItemClick: (Booking) -> Unit
) : ListAdapter<Booking, BookingsAdapter.BookingViewHolder>(BookingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(private val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvCarName.text = booking.carName

            val startStr = DateFormat.format("dd.MM.yyyy", Date(booking.startDate))
            val endStr = DateFormat.format("dd.MM.yyyy", Date(booking.endDate))
            binding.tvDates.text = binding.root.context.getString(R.string.booking_dates_format, startStr, endStr)

            val (statusText, colorRes) = when (booking.status) {
                BookingStatus.ACTIVE -> R.string.booking_status_active to R.color.primary_purple
                BookingStatus.COMPLETED -> R.string.booking_status_completed to R.color.text_secondary
                BookingStatus.CANCELLED -> R.string.booking_status_cancelled to R.color.text_secondary
            }

            binding.tvStatus.setText(statusText)
            binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))

            binding.root.setOnClickListener { onItemClick(booking) }
        }
    }

    class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Booking, newItem: Booking) = oldItem == newItem
    }
}