package com.haire.ui.openjobs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.data.Jobs
import com.haire.databinding.ItemJobsBinding
import java.util.Locale

class JobAdapter(
    private var listJobs: List<Jobs>,
    private val onItemClick: (Jobs) -> Unit
) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>(), Filterable {
    private var filteredList: List<Jobs> = listJobs

    inner class JobViewHolder(private var binding: ItemJobsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jobs: Jobs) {
            binding.apply {
                Glide.with(root.context)
                    .load(jobs.image)
                    .into(ivJobs)
                tvTitle.text = jobs.pekerjaan
                tvAddres.text = jobs.alamat
            }
            itemView.setOnClickListener {
                onItemClick(jobs)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().toLowerCase(Locale.getDefault())
                val filteredList = ArrayList<Jobs>()
                for (item in listJobs) {
                    if (item.pekerjaan.toLowerCase(Locale.getDefault()).contains(query)) {
                        filteredList.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredList = results.values as List<Jobs>
                notifyDataSetChanged()
            }
        }
    }
}