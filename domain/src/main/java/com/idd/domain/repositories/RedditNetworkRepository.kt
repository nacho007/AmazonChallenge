package com.idd.domain.repositories

import com.idd.domain.models.ResultWrapper
import com.idd.domain.models.reddit.RedditResponse

interface RedditNetworkRepository {
    suspend fun getReddits(pageSize: Int, after: String?): ResultWrapper<RedditResponse>
    fun updatePostStatus(id: String)
    fun getVisitedPosts(): List<String>
}