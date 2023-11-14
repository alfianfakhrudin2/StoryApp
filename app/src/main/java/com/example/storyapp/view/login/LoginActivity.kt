package com.example.storyapp.view.login

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
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginViewModel
import com.example.storyapp.data.remote.response.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        setupView()
        playAnimation()
        login()
    }

    fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -60F, 60F).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextViewLogin, View.ALPHA, 1f).apply {
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

        val formInput4 = ObjectAnimator.ofFloat(binding.emailEditTextLogin, View.ALPHA, 1f).apply {
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
        val formInput6 =
            ObjectAnimator.ofFloat(binding.passwordEditTextLogin, View.ALPHA, 1f).apply {
                duration = 200
                startDelay = 100
            }

        val loginBtn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }
        val txtSignup = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).apply {
            duration = 200
            startDelay = 100
        }


        AnimatorSet().apply {
            playSequentially(
                title,
                txtview2,
                formInput3,
                formInput4,
                txtview3,
                formInput5,
                formInput6,
                loginBtn,
                txtSignup
            )
            start()
        }
    }

    private fun login() {
        with(binding) {
            loginButton.setOnClickListener {
                val email = emailEditTextLogin.text.toString()
                val password = passwordEditTextLogin.text.toString()
                viewModel.login(email, password).observe(this@LoginActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            setupFail(result.error, "")
                            Log.e("Erorcuy", result.error)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            result.data.loginResult?.token?.let { it1 ->
                                setupAction(
                                    result.data.message!!,
                                    it1
                                )
                            }
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

    private fun setupAction(message: String, token: String) {

        val email = binding.emailEditTextLogin.text.toString()
        viewModel.saveSession(UserModel(email, token))

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun setupFail(message: String, token: String) {
        val email = binding.emailEditTextLogin.text.toString()
        viewModel.saveSession(UserModel(email, token))
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            create()
            show()
        }
    }
}