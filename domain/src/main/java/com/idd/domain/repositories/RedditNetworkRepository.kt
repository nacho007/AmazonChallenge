package com.idd.domain.repositories

import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.models.ResultWrapper

interface RedditNetworkRepository {
    suspend fun getReddits(): ResultWrapper<RedditResponse>
    fun updatePostStatus(id: String)
}