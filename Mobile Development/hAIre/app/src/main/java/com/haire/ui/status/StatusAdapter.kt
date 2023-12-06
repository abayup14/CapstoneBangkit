package com.haire.ui.status

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.R
import com.haire.data.Jobs
import com.haire.data.Status
import com.haire.databinding.ItemStatusBinding
import java.util.Locale

class StatusAdapter(
    private var listStatus: List<Status>,
    private val onItemClick: (Jobs) -> Unit
) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>(), Filterable {
    private var filteredList: List<Status> = listStatus

    inner class StatusViewHolder(private var binding: ItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobs: Jobs, status: Status) {
            binding.apply {
                Glide.with(root.context)
                    .load(jobs.image)
                    .into(ivJobs)
                tvTitle.text = jobs.pekerjaan
                tvAddres.text = jobs.alamat
                tvStatus.text = status.status
                when (status.status) {
                    "Accepted" -> tvStatus.setBackgroundResource(R.drawable.status_accept)
                    "Rejected" -> tvStatus.setBackgroundResource(R.drawable.status_reject)
                    else -> {
                        tvStatus.setBackgroundResource(R.drawable.bg_status)
                    }
                }
            }
            itemView.setOnClickListener {
                onItemClick(jobs)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size
    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(filteredList[position].jobs, filteredList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<Status>()
                for (item in listStatus) {
                    if (item.jobs.pekerjaan.toLowerCase(Locale.getDefault()).contains(query)) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as List<Status>
                notifyDataSetChanged()
            }
        }
    }
}