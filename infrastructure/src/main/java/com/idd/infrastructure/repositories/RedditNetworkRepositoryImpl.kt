package com.idd.infrastructure.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idd.domain.models.ResultWrapper
import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.repositories.RedditNetworkRepository
import com.idd.infrastructure.clients.RedditClient
import com.idd.infrastructure.network.ResponseHandler

/**
 * Created by ignaciodeandreisdenis on 1/8/21.
 */
class RedditNetworkRepositoryImpl(
    private val client: RedditClient,
    private val responseHandler: ResponseHandler,
    private val sharedPreferences: SharedPreferences,
    private val gSon: Gson
) : RedditNetworkRepository {

    override suspend fun getReddits(): ResultWrapper<RedditResponse> {
        return responseHandler {
            client.getTopReddits().toRedditResponse()
        }
    }

    override fun updatePostStatus(id: String) {
        val idList = returnIdList()

        if (!idList.contains(id)) {
            idList.add(id)
        }

        sharedPreferences.edit().putString(IDS, gSon.toJson(idList)).apply()
    }

    override fun getVisitedPosts(): List<String> {
        val listString = sharedPreferences.getString(RedditLocalRepositoryImpl.IDS, "")
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return if (listString.isNullOrEmpty()) {
            arrayListOf()
        } else {
            gSon.fromJson(listString, type)
        }
    }

    private fun returnIdList(): ArrayList<String> {
        val listString = sharedPreferences.getString(IDS, "")
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return if (listString.isNullOrEmpty()) {
            arrayListOf()
        } else {
            gSon.fromJson(listString, type)
        }
    }

    companion object {
        const val IDS = "IDS"
    }
}
