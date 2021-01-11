package com.idd.infrastructure.clients

import com.idd.infrastructure.entities.RedditEntity
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RedditClient {
    @GET("top.json")
    suspend fun getTopReddits(@QueryMap options: Map<String, String>): RedditEntity
}