package com.dicoding.githubuser.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.UserRepository
class FollowViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserFollowings(username: String) = userRepository.getUserFollowings(username)

    fun getUserFollowers(username: String) = userRepository.getUserFollowers(username)
}