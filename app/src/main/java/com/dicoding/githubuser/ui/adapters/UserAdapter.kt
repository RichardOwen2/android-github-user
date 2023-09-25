package com.dicoding.githubuser.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context
import android.content.Intent
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ItemRowUserBinding
import com.dicoding.githubuser.ui.activities.DetailActivity

class UserAdapter: ListAdapter<UserEntity, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemRowUserBinding, private val context: Context)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity){
            Glide.with(context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.userPhoto)

            binding.userName.text = user.login

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                }

                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.login == newItem.login
            }
            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.login == newItem.login
            }
        }
    }
}