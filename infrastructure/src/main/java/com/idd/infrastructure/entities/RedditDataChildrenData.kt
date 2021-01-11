package com.idd.infrastructure.entities

import com.google.gson.annotations.SerializedName
import com.idd.domain.models.reddit.RedditResponseDataChildrenData

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditDataChildrenData(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String?,
    @SerializedName("created") val created: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("url") val url: String,
    @SerializedName("num_comments") val numComments: Int
) {
    fun toRedditResponseDataChildrenData(): RedditResponseDataChildrenData {
        return RedditResponseDataChildrenData(
            id = id,
            title = title,
            author = author,
            created = created,
            thumbnail = thumbnail,
            url = url,
            numComments = numComments
        )
    }
}
