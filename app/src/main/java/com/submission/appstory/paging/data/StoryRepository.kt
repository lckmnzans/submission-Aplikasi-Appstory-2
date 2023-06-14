package com.submission.appstory.paging.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.submission.appstory.api.ApiService
import com.submission.appstory.paging.database.StoryDatabase
import com.submission.appstory.response.StoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class StoryRepository(private val apiService: ApiService) {
    fun getStory(): Flow<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).flow
    }
}