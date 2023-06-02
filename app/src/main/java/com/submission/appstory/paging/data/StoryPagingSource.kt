package com.submission.appstory.paging.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.submission.appstory.api.ApiService
import com.submission.appstory.response.StoryItem

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(page, params.loadSize)
            LoadResult.Page(
                data = response.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}