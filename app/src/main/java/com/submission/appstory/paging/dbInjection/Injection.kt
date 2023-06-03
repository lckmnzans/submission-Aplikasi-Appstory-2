package com.submission.appstory.paging.dbInjection

import android.content.Context
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.getToken
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.paging.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(getToken(context))
        return StoryRepository(database, apiService)
    }
}