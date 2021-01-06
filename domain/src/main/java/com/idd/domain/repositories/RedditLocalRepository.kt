package com.idd.domain.repositories

import com.idd.domain.models.RedditResponse

interface RedditLocalRepository {
    fun getReddits(): RedditResponse
    fun updatePostStatus(id: String)
}