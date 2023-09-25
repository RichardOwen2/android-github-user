package com.dicoding.githubuser.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.githubuser.data.SettingPreferences
import com.dicoding.githubuser.data.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
    private val pref: SettingPreferences
) : ViewModel() {
    fun findUsers(query: String) = userRepository.findUsers(query)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}