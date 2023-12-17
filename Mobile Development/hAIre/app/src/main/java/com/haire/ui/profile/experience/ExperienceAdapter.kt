package com.haire.ui.profile.experience

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haire.ListPengalamanQuery
import com.haire.databinding.ItemExpBinding

class ExperienceAdapter(
    private var listExp: List<ListPengalamanQuery.Pengalaman?>
) :
    RecyclerView.Adapter<ExperienceAdapter.ExpViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperienceAdapter.ExpViewHolder {
        val binding = ItemExpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpViewHolder(binding)
    }

    override fun getItemCount(): Int = listExp.size

    override fun onBindViewHolder(holder: ExperienceAdapter.ExpViewHolder, position: Int) {
        holder.bind(listExp[position]!!)
    }

    inner class ExpViewHolder(private var binding: ItemExpBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ListPengalamanQuery.Pengalaman) {
            binding.apply {
                tvTitle.text = user.nama_pekerjaan
                tvStartDate.text = user.tgl_mulai
                tvEndDate.text = user.tgl_selesai
            }
        }
    }
}