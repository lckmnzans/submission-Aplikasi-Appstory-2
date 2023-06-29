package com.submission.appstory.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.submission.appstory.utils.MainCoroutineRule
import com.submission.appstory.utils.PagingDataTestSource
import com.submission.appstory.utils.getOrAwaitValue
import com.submission.appstory.paging.adapter.StoryListAdapter
import com.submission.appstory.paging.data.StoryRepository
import com.submission.appstory.response.StoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `when get story should not null and return right data`() = runTest {
        val dummyStoryItem = DummyData.genDummyListOfStory()
        val dummyData = PagingDataTestSource.snapshot(dummyStoryItem)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = dummyData

        `when`(storyRepository.getStory()).thenReturn(expectedStory.asFlow())

        mainViewModel = MainViewModel(storyRepository)
        val actualStory = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )

        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(storyRepository).getStory()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoryItem.size, differ.snapshot().size)
        Assert.assertEquals(dummyStoryItem[0], differ.snapshot()[0])
    }

    @Test
    fun `when get empty story should return no data`() = runTest {
        val dummyEmptyData = PagingData.from(emptyList<StoryItem>())
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = dummyEmptyData

        `when`(storyRepository.getStory()).thenReturn(expectedStory.asFlow())

        mainViewModel = MainViewModel(storyRepository)
        val actualStory = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )

        differ.submitData(actualStory)

        advanceUntilIdle()
        Mockito.verify(storyRepository).getStory()
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}