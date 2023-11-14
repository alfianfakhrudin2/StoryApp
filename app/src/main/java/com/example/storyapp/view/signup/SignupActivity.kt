package com.example.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.data.remote.response.Result
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        setupView()
        playAnimation()
        register()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -60F, 60F).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.titleTextViewSingup, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val txtview1 = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val formInput1 = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val formInput2 = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val txtview2 = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val formInput3 = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        // SIngup
        val formInput4 = ObjectAnimator.ofFloat(binding.emailEditTextSignup, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }

        val txtview3 = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val formInput5 =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).apply {
                duration = 200
                startDelay = 100
            }
        // SIgnup
        val formInput6 =
            ObjectAnimator.ofFloat(binding.passwordEditTextSignup, View.ALPHA, 1f).apply {
                duration = 200
                startDelay = 100
            }

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }

        val login = ObjectAnimator.ofFloat(binding.loginSignup, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                txtview1,
                formInput1,
                formInput2,
                txtview2,
                formInput3,
                formInput4,
                txtview3,
                formInput5,
                formInput6,
                signup,
                login
            )
            start()
        }
    }


    private fun register() {
        with(binding) {
            signupButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditTextSignup.text.toString()
                val password = passwordEditTextSignup.text.toString()
                viewModel.register(name, email, password).observe(this@SignupActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            setupFail(result.error)
                            Log.e("Erorcuy", result.error)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            setupAction(result.data.message!!)
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Yeay!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun setupFail(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Yahhh!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
            }
            create()
            show()
        }
    }
}