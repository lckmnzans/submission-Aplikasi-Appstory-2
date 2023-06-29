package com.submission.appstory.paging

import android.content.Context
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.getToken
import com.submission.appstory.paging.data.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService(getToken(context))
        return StoryRepository(apiService)
    }
}