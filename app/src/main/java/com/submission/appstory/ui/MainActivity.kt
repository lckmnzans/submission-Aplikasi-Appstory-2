package com.submission.appstory.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.appstory.R
import com.submission.appstory.databinding.ActivityMainBinding
import com.submission.appstory.paging.adapter.LoadingStateAdapter
import com.submission.appstory.paging.adapter.StoryListAdapter
import com.submission.appstory.Story
import com.submission.appstory.viewModel.MainViewModel
import com.submission.appstory.viewModel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStories.layoutManager = LinearLayoutManager(this)
        getData()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                intent.putExtra("origin", "FromActionBar")
                startActivity(intent)
            }
            R.id.menu_setting -> {
                getSharedPreferences("LoginSession", Context.MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
        return true
    }

    private fun getData() {
        val adapter = StoryListAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickCallback(object: StoryListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, data)
                startActivity(intent)
            }

            override fun onItemMapsClicked(data: Story) {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                intent.putExtra("origin", "FromListStory")
                intent.putExtra(MapsActivity.EXTRA_STORY, data)
                startActivity(intent)
            }
        })
    }
}