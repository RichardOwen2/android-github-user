package com.dicoding.githubuser.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.Result
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.remote.response.UserDetail
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.ui.viewmodels.DetailViewModel
import com.dicoding.githubuser.ui.adapters.SectionsPagerAdapter
import com.dicoding.githubuser.ui.viewmodels.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME) as String

        val sectionsPagerAdapter = SectionsPagerAdapter(this).apply {
            setUsername(username)
        }

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getDetailUser(username).observe(this) { userDetail ->
            if (userDetail != null) {
                when (userDetail) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setUserDetail(userDetail.data)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Snackbar.make(binding.root, userDetail.error, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        detailViewModel.isUserFavorited(username).observe(this) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    setFavoriteIcon(it.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    Snackbar.make(binding.root, it.error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            detailViewModel.toggleFavoriteUser(username)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserDetail(userDetail: UserDetail) {
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.userPhoto)

        binding.tvUserName.text = "${userDetail.login} (${userDetail.name ?: "no name alias"})"
        binding.tvFollower.text = "${userDetail.followers} Followers"
        binding.tvFollowing.text = "${userDetail.following} Followings"
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    companion object {
        const val EXTRA_USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}