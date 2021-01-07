package com.idd.amazonchallenge.utils

import com.idd.domain.models.RedditResponse
import com.idd.domain.models.RedditResponseData

object DomainObjectsMocks {

    fun getRedditResponse(): RedditResponse {
        return RedditResponse(
            "kind",
            RedditResponseData(
                "modhash", null, "", ""
            )
        )
    }
}