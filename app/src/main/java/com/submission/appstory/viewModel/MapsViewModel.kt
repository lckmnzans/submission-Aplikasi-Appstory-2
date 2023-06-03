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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel(context: Context): ViewModel() {

    private val _listStories = MutableLiveData<List<StoryItem>>()
    val listStories: LiveData<List<StoryItem>> = _listStories

    private fun getStoriesWithLoc(token: String) {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                val client = ApiConfig.getApiService(token).getStoriesWithLoc()
                val response = client.listStory
                withContext(Dispatchers.Main) {
                    _listStories.value = response
                }
                Log.d(TAG, response.toString())
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    init {
        getStoriesWithLoc(getToken(context))
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}