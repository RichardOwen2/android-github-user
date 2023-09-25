package com.dicoding.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity (
    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,

    @field:ColumnInfo(name = "favorite")
    var favorite: Boolean = false
)