package com.idd.domain.actions

import com.idd.domain.models.ErrorResponse
import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.models.ResultWrapper
import com.idd.domain.repositories.RedditNetworkRepository

/**
 * Created by ignaciodeandreisdenis on 1/8/21.
 */
class GetNetWorkRedditEntriesAction(
    private val redditNetworkRepository: RedditNetworkRepository
) {
    sealed class Result {
        data class Success(val value: RedditResponse) : Result()
        data class Error(val value: ErrorResponse? = null) : Result()
        object NetworkError : Result()
    }

    suspend operator fun invoke(): Result {
        return when (val result = redditNetworkRepository.getReddits()) {
            is ResultWrapper.Success -> Result.Success(result.value)
            is ResultWrapper.NetworkError -> Result.NetworkError
            is ResultWrapper.Error -> Result.Error(result.error)
            else -> Result.Error()
        }
    }

    fun updatePostStatus(id: String) {
        redditNetworkRepository.updatePostStatus(id)
    }
}