package com.idd.domain.models

import java.io.Serializable

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
data class RedditParams(val author: String, val avatarUrl: String, val title: String) : Serializable
