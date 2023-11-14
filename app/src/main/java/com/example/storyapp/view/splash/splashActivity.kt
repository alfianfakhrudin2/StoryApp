package com.example.storyapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.storyapp.R
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@splashActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}