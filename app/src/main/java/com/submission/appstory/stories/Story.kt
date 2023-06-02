package com.submission.appstory.stories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val userName: String,
    val avtUrl: String,
    val desc: String,
): Parcelable
