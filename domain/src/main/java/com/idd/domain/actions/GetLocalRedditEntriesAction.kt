package com.idd.domain.actions

import com.idd.domain.models.RedditResponse
import com.idd.domain.repositories.RedditLocalRepository

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class GetLocalRedditEntriesAction(
    private val redditLocalRepository: RedditLocalRepository
) {
    sealed class Result {
        data class Success(val value: RedditResponse) : Result()
    }

    operator fun invoke(): Result {
        return Result.Success(redditLocalRepository.getReddits())
    }

    fun updatePostStatus(id: String) {
        redditLocalRepository.updatePostStatus(id)
    }
}
