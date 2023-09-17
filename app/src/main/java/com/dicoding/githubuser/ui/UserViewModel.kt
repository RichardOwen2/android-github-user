package com.dicoding.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.response.UserResponse
import com.dicoding.githubuser.data.response.UserList
import com.dicoding.githubuser.data.retrofit.ApiConfig
import com.dicoding.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<List<UserList>>()
    val user: LiveData<List<UserList>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        findUser()
    }

    fun findUser(query: String = "RichardOwen2") {
        if (query == "") {
            _snackbarText.value = Event("Fail: Search Tidak Boleh Kosong")
            return
        }

        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items

                    if (items.isNullOrEmpty()) {
                        _snackbarText.value = Event("Fail: User Tidak ditemukan")
                        return
                    }

                    _user.value = response.body()?.items
                } else {
                    val errorMessage = response.errorBody()?.string()
                    _snackbarText.value = Event("Fail: $errorMessage")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Fail: ${t.message.toString()}")
            }
        })
    }
}