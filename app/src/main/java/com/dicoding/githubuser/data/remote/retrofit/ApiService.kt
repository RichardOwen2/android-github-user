package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.githubuser.data.remote.response.UserDetail
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}