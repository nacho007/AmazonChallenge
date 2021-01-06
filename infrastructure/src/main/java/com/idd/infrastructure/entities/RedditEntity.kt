package com.idd.infrastructure.entities

import com.google.gson.annotations.SerializedName
import com.idd.domain.models.RedditResponse

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditEntity(
    @SerializedName("kind") val kind: String,
    @SerializedName("data") val data: RedditDataEntity
) {
    fun toRedditResponse(): RedditResponse {
        return RedditResponse(kind = kind, redditResponseData = data.toRedditResponseData())
    }
}
