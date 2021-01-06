package com.idd.domain.models

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditResponseDataChildrenData(
    val id: String,
    val title: String,
    val author: String?,
    val created: Int,
    val thumbnail: String,
    val url: String,
    val numComments: Int,
    var visited: Boolean
)
