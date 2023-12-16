package com.haire.ui.company.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haire.data.User
import com.haire.databinding.ItemUserBinding

class UserAdapter(private var listUser: List<User>, private val onAcceptClick: () -> Unit, private val onRejectClick: () -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapter.UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    inner class UserViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(root.context)
                    .load(user.photoUrl)
                    .circleCrop()
                    .into(ivProfile)
                tvNama.text = user.name
                btnAccept.setOnClickListener {
                    onAcceptClick()
                }
                btnReject.setOnClickListener {
                    onRejectClick()
                }
            }
        }
    }
}