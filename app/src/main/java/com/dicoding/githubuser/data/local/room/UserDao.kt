package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubuser.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<UserEntity>>

    @Query("DELETE FROM user WHERE favorite = 0")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(users: List<UserEntity>)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login)")
    fun isUserExist(login: String): Boolean

    @Query("SELECT * FROM user WHERE favorite = 1")
    fun getFavoriedUsers(): LiveData<List<UserEntity>>

    @Query("UPDATE user SET favorite = :favorite WHERE login = :login")
    fun updateFavoriteUser(login: String, favorite: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login AND favorite = 1)")
    fun isUserFavorited(login: String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login AND favorite = 1)")
    fun isUserFavoritedLiveData(login: String): LiveData<Boolean>
}