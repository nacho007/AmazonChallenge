package com.idd.infrastructure.repositories

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.idd.domain.models.ResultWrapper
import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.repositories.RedditLocalRepository
import com.idd.infrastructure.entities.RedditEntity
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class RedditLocalRepositoryImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val gSon: Gson
) : RedditLocalRepository {

    override fun getReddits(): ResultWrapper<RedditResponse> {
        val reddits = loadJSONFromAsset(context, "top.json")
        val redditEntity = Gson().fromJson(reddits, RedditEntity::class.java)

        val response = redditEntity.toRedditResponse()
        val visitedPostIds = returnIdList()

        response.redditResponseData.children?.forEach {
            if (visitedPostIds.contains(it.data.id)) {
                it.data.visited = true
            }
        }

        return ResultWrapper.Success(response)
    }

    private fun loadJSONFromAsset(context: Context, file: String?): String? {
        val json: String
        json = try {
            val `is` = context.assets.open(file!!)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override fun updatePostStatus(id: String) {
        val idList = returnIdList()

        if (!idList.contains(id)) {
            idList.add(id)
        }

        sharedPreferences.edit().putString(IDS, gSon.toJson(idList)).apply()
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
        const val USER_PREFERENCES = "user_preferences"
        const val IDS = "IDS"
    }
}