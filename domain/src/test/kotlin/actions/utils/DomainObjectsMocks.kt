package actions.utils

import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.models.reddit.RedditResponseData

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