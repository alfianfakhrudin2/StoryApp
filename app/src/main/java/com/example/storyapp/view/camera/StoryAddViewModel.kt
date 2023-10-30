package com.example.storyapp.view.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.example.storyapp.data.repository.UserRepository
import java.io.File

class StoryAddViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String, lat: Double? = null, lon: Double? = null) =
        repository.uploadImage(file, description, lat, lon)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}