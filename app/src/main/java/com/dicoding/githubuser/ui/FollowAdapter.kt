package com.dicoding.githubuser.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.data.response.UserList
import com.dicoding.githubuser.databinding.ItemRowFollowBinding

class FollowAdapter: ListAdapter<UserList, FollowAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemRowFollowBinding, private val context: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserList) {
            Glide.with(context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userPhoto)

            binding.userName.text = user.login
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserList>() {
            override fun areItemsTheSame(oldItem: UserList, newItem: UserList): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: UserList, newItem: UserList): Boolean {
                return oldItem == newItem
            }
        }
    }

}