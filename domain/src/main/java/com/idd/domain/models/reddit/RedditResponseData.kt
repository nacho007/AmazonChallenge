package com.idd.domain.models.reddit

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditResponseData(
    val modhash: String,
    val children: List<RedditResponseDataChildren>?,
    val after: String?,
    val before: String?
)
