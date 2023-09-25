package com.dicoding.githubuser.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.UserRepository

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getDetailUser(username: String) = userRepository.getDetailUser(username)

    fun isUserFavorited(username: String) = userRepository.isUserFavorited(username)

    fun toggleFavoriteUser(username: String, avatarUrl: String?) = userRepository.toggleFavoriteUser(username, avatarUrl)
}