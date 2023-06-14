package com.submission.appstory.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.submission.appstory.databinding.ActivityDetailBinding
import com.submission.appstory.Story

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        val story = getParcelableData()
        if (story != null) {
            setStoryDetail(story)
        }
    }

    private fun setStoryDetail(story: Story) {
        Glide.with(this@DetailActivity).load(story.avtUrl).into(binding.ivUserPhoto)
        binding.tvUserName.text = story.userName
        binding.tvUserDesc.text = story.desc
    }

    @Suppress("DEPRECATION")
    private fun getParcelableData(): Story? {
        return if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, Story::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }
    }
}