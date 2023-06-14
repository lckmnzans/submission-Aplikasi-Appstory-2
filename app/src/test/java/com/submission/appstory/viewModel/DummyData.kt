package com.submission.appstory.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.submission.appstory.response.StoryItem

object DummyData {

    fun genDummyListOfStory(): List<StoryItem> {
        val items: MutableList<StoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = StoryItem(
                "Photo URL Story $i",
                "2022-02-22T22:22:22Z",
                "Story $i ",
                "Story Description",
                "$i",
                "0.2",
                "0.1"
            )
            items.add(story)
        }
        return items
    }
}