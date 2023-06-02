package com.submission.appstory.paging.dbInjection

import android.content.Context
import android.util.Log
import com.submission.appstory.api.ApiConfig
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.paging.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}