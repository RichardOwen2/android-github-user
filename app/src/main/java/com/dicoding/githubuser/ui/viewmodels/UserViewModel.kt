package com.dicoding.githubuser.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun findUsers(query: String) = userRepository.findUsers(query)
}