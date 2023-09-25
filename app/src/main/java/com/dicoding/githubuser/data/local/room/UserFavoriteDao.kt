package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubuser.data.local.entity.UserEntity

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("DELETE FROM user where login = :login")
    fun delete(login: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(users: UserEntity)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login)")
    fun isUserFavorited(login: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login)")
    fun isUserFavoritedLiveData(login: String): LiveData<Boolean>
}