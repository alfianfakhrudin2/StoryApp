package com.example.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.remote.response.RegisterResponse
import com.example.storyapp.data.remote.response.Result
import com.example.storyapp.data.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, pass: String): LiveData<Result<RegisterResponse>> =
        repository.register(name, email, pass)
}