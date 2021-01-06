package com.idd.infrastructure.entities

import com.google.gson.annotations.SerializedName
import com.idd.domain.models.RedditResponseDataChildren

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditDataChildren(
    @SerializedName("kind") val kind: String,
    @SerializedName("data") val data: RedditDataChildrenData
) {
    fun toRedditResponseDataChildren(): RedditResponseDataChildren {
        return RedditResponseDataChildren(kind = kind, data = data.toRedditResponseDataChildrenData())
    }
}
