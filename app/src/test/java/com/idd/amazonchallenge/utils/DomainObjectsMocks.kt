package com.idd.amazonchallenge.utils

import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.models.reddit.RedditResponseData
import com.idd.domain.models.reddit.RedditResponseDataChildren
import com.idd.domain.models.reddit.RedditResponseDataChildrenData

object DomainObjectsMocks {

    fun getRedditResponse(): RedditResponse {
        return RedditResponse(
            "kind",
            RedditResponseData(
                "modhash", mutableListOf(
                    RedditResponseDataChildren(
                        "kind",
                        RedditResponseDataChildrenData(
                            id = "a34casd",
                            title = "Title",
                            author = "Author",
                            created = 1411959977,
                            thumbnail = "http://b.thumbs.redditmedia.com/X7cgaQSlD4XBfJnyaeuvuurtPLqAZbwlf3A1KfDFebk.jpg",
                            url = "http://i.imgur.com/XwdW1PG.jpg",
                            numComments = 123,
                            readPost = false,
                            isLoading = false
                        )
                    )
                ), "", ""
            )
        )
    }
}