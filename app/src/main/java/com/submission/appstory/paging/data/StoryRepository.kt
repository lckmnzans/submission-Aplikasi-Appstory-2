package com.submission.appstory.paging.data

import androidx.paging.*
import com.submission.appstory.api.ApiService
import com.submission.appstory.response.StoryItem
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