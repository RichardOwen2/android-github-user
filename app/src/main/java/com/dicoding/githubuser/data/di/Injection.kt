package com.dicoding.githubuser.data.di

import android.content.Context
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.local.room.FavoriteUserDatabase
import com.dicoding.githubuser.data.local.room.UserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userDatabase = UserDatabase.getInstance(context)
        val userDao = userDatabase.userDao()
        val favoriteUserDatabase = FavoriteUserDatabase.getInstance(context)
        val favoriteUserDao = favoriteUserDatabase.userFavoriteDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, userDao, favoriteUserDao, appExecutors)
    }
}