package com.example.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.example.storyapp.data.lokal.database.StoryDatabase
import com.example.storyapp.data.remote.retrofit.ApiConfig
import com.example.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.prefs.Preferences

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }

        val database = StoryDatabase.getDatabase(context)

        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref, database)

    }
}