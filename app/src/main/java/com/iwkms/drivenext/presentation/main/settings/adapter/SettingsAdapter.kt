package com.iwkms.drivenext.presentation.main.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwkms.drivenext.databinding.ItemSettingBinding
import com.iwkms.drivenext.presentation.main.settings.model.SettingsItem

class SettingsAdapter(
    private val onItemClick: (SettingsItem) -> Unit
) : ListAdapter<SettingsItem, SettingsAdapter.SettingsViewHolder>(SettingsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val binding = ItemSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SettingsViewHolder(private val binding: ItemSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem) {
            binding.ivIcon.setImageResource(item.iconResId)
            binding.tvTitle.setText(item.titleResId)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class SettingsDiffCallback : DiffUtil.ItemCallback<SettingsItem>() {
        override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
            return oldItem.titleResId == newItem.titleResId
        }

        override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
            return oldItem == newItem
        }
    }
}