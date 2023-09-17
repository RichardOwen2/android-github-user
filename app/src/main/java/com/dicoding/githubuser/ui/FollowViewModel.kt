package com.dicoding.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.UserList
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _user = MutableLiveData<List<UserList>>()
    val user: LiveData<List<UserList>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getUserFollowings(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserList>> {
            override fun onResponse(
                call: Call<List<UserList>>,
                response: Response<List<UserList>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _snackbarText.value = Event("Fail: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<UserList>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Fail: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserList>> {
            override fun onResponse(
                call: Call<List<UserList>>,
                response: Response<List<UserList>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _snackbarText.value = Event("Fail: $errorMessage")
                }
            }

            override fun onFailure(call: Call<List<UserList>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Fail: ${t.message.toString()}")
            }
        })
    }
}