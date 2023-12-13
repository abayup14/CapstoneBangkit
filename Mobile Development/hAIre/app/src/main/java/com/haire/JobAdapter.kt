package com.haire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.databinding.ItemJobsBinding
import com.haire.model.Jobs

class JobAdapter(private val listJobs: List<Jobs>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {
    private lateinit var onItemClickCall: OnItemClickCall
    class JobViewHolder(var binding: ItemJobsBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCall(onItemClickCall: OnItemClickCall) {
        this.onItemClickCall = onItemClickCall
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun getItemCount(): Int = listJobs.size

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val jobs = listJobs[position]

        holder.binding.apply {
            Glide.with(root.context)
                .load(jobs.image)
                .into(ivJobs)
            tvTitle.text = jobs.pekerjaan
            tvAddres.text = jobs.alamat
        }
    }

    interface OnItemClickCall {
        fun onItemClicked(data: Jobs)
    }
}