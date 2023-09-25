package com.dicoding.githubuser.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.SettingPreferences
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.dataStore
import com.dicoding.githubuser.data.di.Injection

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val pref: SettingPreferences
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) -> MainViewModel(userRepository, pref)
            isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(userRepository)
            isAssignableFrom(FollowViewModel::class.java) -> FollowViewModel(userRepository)
            isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(userRepository)
            isAssignableFrom(SettingViewModel::class.java) -> SettingViewModel(pref)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideRepository(context),
                SettingPreferences.getInstance(context.dataStore)
            )
        }.also { instance = it }
    }
}