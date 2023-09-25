package com.dicoding.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
	@field:SerializedName("items")
	val items: List<User>,
)

data class User(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,
)
