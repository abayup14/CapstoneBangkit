package com.haire.ui.status

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.ListApplyLowonganQuery
import com.haire.ListApplyUserQuery
import com.haire.ListLowonganUserApplyQuery
import com.haire.ProfileCompanyQuery
import com.haire.R
import com.haire.databinding.ItemStatusBinding
import java.util.Locale

class StatusAdapter(
    private var listApply: List<ListApplyUserQuery.Apply?>,
    private var listStatus: List<ListLowonganUserApplyQuery.Lowongan?>,
    private var companies: List<ProfileCompanyQuery.Company?>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>(), Filterable {
    private var filteredList: List<ListLowonganUserApplyQuery.Lowongan?> = listStatus

    inner class StatusViewHolder(private var binding: ItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            apply: ListApplyUserQuery.Apply?,
            jobs: ListLowonganUserApplyQuery.Lowongan,
            company: ProfileCompanyQuery.Company
        ) {
            binding.apply {
                if (company.url_photo != "") {
                    Glide.with(root.context)
                        .load(company.url_photo)
                        .circleCrop()
                        .into(ivJobs)
                }
                tvTitle.text = jobs.nama
                tvAddres.text = company.alamat
                when (apply?.status) {
                    "Diterima" -> {
                        tvStatus.setBackgroundResource(R.drawable.status_accept)
                        tvStatus.text = "Accepted"
                    }
                    "Ditolak" -> {
                        tvStatus.setBackgroundResource(R.drawable.status_reject)
                        tvStatus.text = "Rejected"
                    }
                    else -> {
                        tvStatus.setBackgroundResource(R.drawable.bg_status)
                    }
                }
            }
            itemView.setOnClickListener {
                onItemClick(jobs.id ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size
    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(listApply[position], filteredList[position]!!, companies[position]!!)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<ListLowonganUserApplyQuery.Lowongan>()
                for (item in listStatus) {
                    if (item?.nama?.toLowerCase(Locale.getDefault())?.contains(query) == true) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as List<ListLowonganUserApplyQuery.Lowongan>
                notifyDataSetChanged()
            }
        }
    }
}