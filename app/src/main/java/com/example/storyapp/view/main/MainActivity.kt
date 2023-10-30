package com.example.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.view.ViewModelFactory
import com.example.storyapp.view.adapter.ListStoryAdapter
import com.example.storyapp.view.adapter.LoadingStateAdapter
import com.example.storyapp.view.camera.StoryAddActivity
import com.example.storyapp.view.map.MapsActivity
import com.example.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var factory: ViewModelFactory
    private var token = ""
    private lateinit var storyAdapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultAdapter = ListStoryAdapter()

        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvMain.addItemDecoration(itemDecoration)

        binding.apply {
            resultAdapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        showLoading(true)
                    }

                    is LoadState.Error -> {
                        showLoading(false)
                    }

                    else -> {
                        showLoading(false)
                    }
                }
            }
            getSession()
        }
        setupView()
        setupViewModel()
    }

    private fun getSession() {
        setData()
        mainViewModel.getSession().observe(this) {
            token = it.token
            if (!it.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.quote.observe(this@MainActivity) { pagingData ->
                    storyAdapter.submitData(lifecycle, pagingData)
                }
                showLoading(false)
            }
        }
    }

    private fun setData() {
        storyAdapter = ListStoryAdapter()
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupView() {

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, StoryAddActivity::class.java)
            startActivity(intent)
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_button -> {
                mainViewModel.logout()
                true
            }

            R.id.map_button -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.language_button -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}