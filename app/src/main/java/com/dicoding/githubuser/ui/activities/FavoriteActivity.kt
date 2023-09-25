package com.dicoding.githubuser.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.Result
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.ui.adapters.UserAdapter
import com.dicoding.githubuser.ui.viewmodels.FavoriteViewModel
import com.dicoding.githubuser.ui.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        favoriteViewModel.getFavoriteUsers().observe(this) { user ->
            if (user != null) {
                when (user) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setUserList(user.data)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Snackbar.make(binding.root, user.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUserList(user: List<UserEntity>) {
        val adapter = UserAdapter().apply {
            submitList(user)
        }
        binding.rvUserList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}