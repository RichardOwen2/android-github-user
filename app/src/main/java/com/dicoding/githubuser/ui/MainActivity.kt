package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.response.UserList
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        with (binding) {
            searchView.apply {
                setupWithSearchBar(searchBar)
                this.editText
                    .setOnEditorActionListener { textView, actionId, event ->
                        searchBar.text = searchView.text
                        userViewModel.findUser(searchView.text.toString())
                        searchView.hide()
                        false
                    }
            }
        }

        userViewModel.user.observe(this) {
            setUserList(it)
        }

        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        userViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUserList(user: List<UserList>) {
        val adapter = UserAdapter().apply {
            submitList(user)
        }
        binding.rvUserList.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}