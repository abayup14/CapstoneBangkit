package com.haire.ui.openjobs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.ListLowongansQuery
import com.haire.data.Jobs
import com.haire.databinding.ItemJobsBinding
import java.util.Locale

class JobAdapter(
    private var listJobs: List<ListLowongansQuery.ListLowongan?>,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>(), Filterable {
    private var filteredList: List<ListLowongansQuery.ListLowongan?> = listJobs

    inner class JobViewHolder(private var binding: ItemJobsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobs: ListLowongansQuery.ListLowongan) {
            binding.apply {
                Glide.with(root.context)
                    .load(jobs.url_photo)
                    .into(ivJobs)
                tvTitle.text = jobs.nama
                tvAddres.text = jobs.deskripsi
            }
            itemView.setOnClickListener {
                onItemClick(jobs.id ?: 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(filteredList[position]!!)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<ListLowongansQuery.ListLowongan>()
                for (item in listJobs) {
                    if (item?.nama?.toLowerCase(Locale.getDefault())?.contains(query) == true) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as List<ListLowongansQuery.ListLowongan>
                notifyDataSetChanged()
            }
        }
    }
}