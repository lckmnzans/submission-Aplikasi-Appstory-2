package com.submission.appstory.viewModel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.appstory.getToken
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.paging.dbInjection.Injection
import com.submission.appstory.response.StoryItem

class MainViewModel(storyRepository: StoryRepository): ViewModel() {
    val story: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    companion object {
        private const val TAG = "MainActivity"
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