package com.idd.domain.actions

import com.idd.domain.models.RedditResponse
import com.idd.domain.models.ResultWrapper
import com.idd.domain.repositories.RedditLocalRepository

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class GetLocalRedditEntriesAction(
    private val redditLocalRepository: RedditLocalRepository
) {
    sealed class Result {
        data class Success(val value: RedditResponse) : Result()
        object Error : Result()
    }

    operator fun invoke(): Result {
        return when (val resultWrapper = redditLocalRepository.getReddits()) {
            is ResultWrapper.Success -> Result.Success(resultWrapper.value)
            is ResultWrapper.Error -> Result.Error
        }
    }

    fun updatePostStatus(id: String) {
        redditLocalRepository.updatePostStatus(id)
    }
}
