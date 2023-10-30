package com.example.storyapp.view.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Detail Page"
        actionbar.setDisplayHomeAsUpEnabled(true)

        setupData()
    }

    private fun setupData() {
        showLoading(true)
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(STORY_ID, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra<ListStoryItem>(STORY_ID) as ListStoryItem
        }
        Log.d("DATACUY", data.toString())
        showLoading(false)
        binding.apply {
            data?.let {
                tvName.text = data.name
                tvDescription.text = data.description
                Glide.with(this@DetailActivity).load(data.photoUrl).fitCenter().into(ivDetail)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}