package com.haire.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haire.ListNotifikasiUserQuery
import com.haire.databinding.ItemNotificationBinding

class NotifikasiAdapter(private val listNotifikasi: List<ListNotifikasiUserQuery.Notifikasi?>) :
    RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {
    inner class NotifikasiViewHolder(private var binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notif: ListNotifikasiUserQuery.Notifikasi?) {
            binding.apply {
                tvTimestamp.text = notif?.waktu
                tvNotificationContent.text = notif?.pesan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifikasiViewHolder(binding)
    }

    override fun getItemCount(): Int = listNotifikasi.size

    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        holder.bind(listNotifikasi[position])
    }
}