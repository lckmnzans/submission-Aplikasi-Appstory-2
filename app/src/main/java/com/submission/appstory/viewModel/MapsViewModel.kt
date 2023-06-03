package com.submission.appstory.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.getToken
import com.submission.appstory.response.StoryItem
import kotlinx.coroutines.launch

class MapsViewModel(context: Context): ViewModel() {

    private val _listStories = MutableLiveData<List<StoryItem>>()
    val listStories: LiveData<List<StoryItem>> = _listStories

    private fun getStoriesWithLoc(token: String, page: Int = 1) {
        viewModelScope.launch {
            val client = ApiConfig.getApiService(token).getStoriesWithLoc(page)
            val response = client.listStory
            _listStories.value = response
            Log.d(TAG, response.toString())
        }
    }

    init {
        getStoriesWithLoc(getToken(context))
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}