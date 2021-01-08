package com.idd.infrastructure.clients

import com.idd.infrastructure.entities.RedditEntity
import retrofit2.http.GET

interface RedditClient {
    @GET("top.json?limit=10")
    suspend fun getTopReddits(): RedditEntity
}