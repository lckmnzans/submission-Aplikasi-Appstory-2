package com.submission.appstory.viewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.response.StoryItem

class MainViewModel(storyRepository: StoryRepository): ViewModel() {

    val story: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    companion object {
        private const val TAG = "MainActivity"
    }
}