package com.idd.infrastructure.entities

import com.google.gson.annotations.SerializedName
import com.idd.domain.models.RedditResponseData
import com.idd.domain.models.RedditResponseDataChildren

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
data class RedditDataEntity(
    @SerializedName("modhash") val modhash: String,
    @SerializedName("children") val children: List<RedditDataChildren>?,
    @SerializedName("after") val after: String?,
    @SerializedName("before") val before: String?
) {
    fun toRedditResponseData(): RedditResponseData {
        return RedditResponseData(
            modhash = modhash,
            children = listToDataChildren(),
            after = after,
            before = before
        )
    }

    private fun listToDataChildren(): List<RedditResponseDataChildren> {
        val response = arrayListOf<RedditResponseDataChildren>()
        children?.forEach { response.add(it.toRedditResponseDataChildren()) }
        return response
    }

}
