package com.dicoding.githubuser.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.Result
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.viewmodels.ViewModelFactory
import com.dicoding.githubuser.ui.adapters.UserAdapter
import com.dicoding.githubuser.ui.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.apply {
                setupWithSearchBar(searchBar)
                this.editText
                    .setOnEditorActionListener { _, _, _ ->
                        searchBar.text = searchView.text
                        userViewModel.findUsers(searchView.text.toString())
                        searchView.hide()
                        false
                    }
            }
        }

        userViewModel.findUsers("RichardOwen2").observe(this) { user ->
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