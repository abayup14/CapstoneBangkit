package com.haire.ui.profile.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haire.ListEdukasiQuery
import com.haire.databinding.ItemExpBinding

class EducationAdapter(
    private var listEd: List<ListEdukasiQuery.Edukasi?>
) :
    RecyclerView.Adapter<EducationAdapter.EduViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EducationAdapter.EduViewHolder {
        val binding = ItemExpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EduViewHolder(binding)
    }

    override fun getItemCount(): Int = listEd.size

    override fun onBindViewHolder(holder: EducationAdapter.EduViewHolder, position: Int) {
        holder.bind(listEd[position]!!)
    }

    inner class EduViewHolder(private var binding: ItemExpBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ListEdukasiQuery.Edukasi) {
            binding.apply {
                tvTitle.text = user.nama_institusi
                tvStartDate.text = user.tgl_awal
                tvEndDate.text = user.tgl_akhir
            }
        }
    }
}