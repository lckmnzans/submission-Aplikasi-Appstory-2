package com.submission.appstory.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.paging.dbInjection.Injection
import com.submission.appstory.response.StoriesResponse
import com.submission.appstory.response.StoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(storyRepository: StoryRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<StoryItem>>()
    val listStory: LiveData<List<StoryItem>> = _listStory

    val story: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    fun getStories(token: String) {
        _isLoading.value = true
        val call = ApiConfig.getApiService().getAllStories()
        call.enqueue(object: Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()!!
                    _listStory.value = responseBody.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
        var TOKEN = ""
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}