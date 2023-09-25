package com.dicoding.githubuser.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.UserRepository

class FavoriteViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUsers() = userRepository.getFavoriteUsers()
}