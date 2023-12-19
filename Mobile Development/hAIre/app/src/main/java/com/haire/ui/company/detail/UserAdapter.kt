package com.haire.ui.company.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haire.ListApplyLowonganQuery
import com.haire.ProfileUserQuery
import com.haire.databinding.ItemUserBinding

class UserAdapter(
    private var listApply: List<ListApplyLowonganQuery.Apply?>,
    private var listUser: List<ProfileUserQuery.User?>,
    private val onAcceptClick: (Int) -> Unit,
    private val onRejectClick: (Int) -> Unit,
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapter.UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = listApply.size

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(listApply[position], listUser[position])
    }

    inner class UserViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(apply: ListApplyLowonganQuery.Apply?, listUser: ProfileUserQuery.User?) {
            binding.apply {
//                Glide.with(root.context)
//                    .load(user.photoUrl)
//                    .circleCrop()
//                    .into(ivProfile)
                tvNama.text = listUser?.nama
                btnAccept.setOnClickListener {
                    onAcceptClick(apply?.user_iduser ?: 0)
                }
                btnReject.setOnClickListener {
                    onRejectClick(apply?.user_iduser ?: 0)
                }
            }
        }
    }

    fun getListApply(): List<ListApplyLowonganQuery.Apply?> {
        return listApply
    }

    fun updateListApply(updatedList: List<ListApplyLowonganQuery.Apply?>) {
        listApply = updatedList
        notifyDataSetChanged()
    }
}