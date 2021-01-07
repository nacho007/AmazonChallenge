package com.idd.domain.repositories

import com.idd.domain.models.RedditResponse
import com.idd.domain.models.ResultWrapper

interface RedditLocalRepository {
    fun getReddits(): ResultWrapper<RedditResponse>
    fun updatePostStatus(id: String)
}