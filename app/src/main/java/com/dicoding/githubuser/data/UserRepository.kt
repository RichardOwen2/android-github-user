package com.dicoding.githubuser.data

import androidx.lifecycle.MediatorLiveData
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.local.room.UserDao
import com.dicoding.githubuser.data.local.room.UserFavoriteDao
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.data.remote.response.UserDetail
import com.dicoding.githubuser.data.remote.response.UserResponse
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.data.remote.retrofit.ApiService
import com.dicoding.githubuser.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val favoriteUserDao: UserFavoriteDao,
    private val appExecutors: AppExecutors
) {
    private val userResult = MediatorLiveData<Result<List<UserEntity>>>()
    private val favoriteResult = MediatorLiveData<Result<List<UserEntity>>>()
    private val detailResult = MediatorLiveData<Result<UserDetail>>()
    private val isFavoriteResult = MediatorLiveData<Result<Boolean>>()
    private val followersResult = MediatorLiveData<Result<List<User>?>>()
    private val followingsResult = MediatorLiveData<Result<List<User>?>>()

    fun findUsers(query: String): MediatorLiveData<Result<List<UserEntity>>> {
        userResult.value = Result.Loading
        val client = apiService.getUsers(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    val userList = ArrayList<UserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val userEntity = UserEntity(
                                user.login,
                                user.avatarUrl
                            )
                            userList.add(userEntity)
                        }
                        userDao.deleteAll()
                        userDao.insertUsers(userList)
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                userResult.value = Result.Error(t.message.toString())
            }
        })
        val localData = userDao.getUsers()
        userResult.addSource(localData) { newData: List<UserEntity> ->
            userResult.value = Result.Success(newData)
        }
        return userResult
    }

    fun getDetailUser(username: String): MediatorLiveData<Result<UserDetail>> {
        detailResult.value = Result.Loading
        val client = apiService.getUserDetail(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        detailResult.value = Result.Success(user)
                    } else {
                        detailResult.value = Result.Error("User not found")
                    }
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                detailResult.value = Result.Error(t.message.toString())
            }
        })
        return detailResult
    }

    fun getUserFollowings(username: String): MediatorLiveData<Result<List<User>?>> {
        followersResult.value = Result.Loading
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>?>
            ) {
                if (response.isSuccessful) {
                    followersResult.value = Result.Success(response.body())
                } else {
                    followersResult.value = Result.Error("User not found")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                followersResult.value = Result.Error(t.message.toString())
            }
        })
        return followersResult
    }

    fun getUserFollowers(username: String): MediatorLiveData<Result<List<User>?>> {
        followingsResult.value = Result.Loading
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>?>
            ) {
                if (response.isSuccessful) {
                    followingsResult.value = Result.Success(response.body())
                } else {
                    followingsResult.value = Result.Error("User not found")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                followingsResult.value = Result.Error(t.message.toString())
            }
        })
        return followingsResult
    }

    fun isUserFavorited(login: String): MediatorLiveData<Result<Boolean>> {
        isFavoriteResult.value = Result.Loading

        try {
            val isFavorited = favoriteUserDao.isUserFavoritedLiveData(login)
            isFavoriteResult.addSource(isFavorited) {
                isFavoriteResult.value = Result.Success(it)
            }
        } catch (e: Exception) {
            isFavoriteResult.value = Result.Error(e.stackTraceToString())
        }

        return isFavoriteResult
    }

    fun toggleFavoriteUser(login: String, avatarUrl: String?) {
        appExecutors.diskIO.execute {
            val favorite = favoriteUserDao.isUserFavorited(login)
            if (favorite) {
                favoriteUserDao.delete(login)
            } else {
                favoriteUserDao.insertUser(UserEntity(login, avatarUrl))
            }
        }
    }

    fun getFavoriteUsers(): MediatorLiveData<Result<List<UserEntity>>> {
        favoriteResult.value = Result.Loading
        val localData = favoriteUserDao.getUsers()
        favoriteResult.addSource(localData) { newData: List<UserEntity> ->
            favoriteResult.value = Result.Success(newData)
        }
        return favoriteResult
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            favoriteUserDao: UserFavoriteDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, favoriteUserDao, appExecutors)
            }.also { instance = it }
    }
}