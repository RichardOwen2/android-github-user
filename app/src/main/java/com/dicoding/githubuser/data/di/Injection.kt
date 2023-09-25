package com.dicoding.githubuser.data.di

import android.content.Context
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.local.room.UserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.util.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}