package com.idd.domain.repositories

import com.idd.domain.models.ResultWrapper
import com.idd.domain.models.reddit.RedditResponse

interface RedditLocalRepository {
    fun getReddits(): ResultWrapper<RedditResponse>
    fun updatePostStatus(id: String)
}