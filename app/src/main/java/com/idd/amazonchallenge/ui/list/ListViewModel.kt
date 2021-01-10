package com.idd.amazonchallenge.ui.list

import androidx.lifecycle.viewModelScope
import com.idd.amazonchallenge.BuildConfig
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.constants.LOCAL
import com.idd.amazonchallenge.constants.NETWORK
import com.idd.amazonchallenge.ui.common.BaseAction
import com.idd.amazonchallenge.ui.common.BaseViewModel
import com.idd.amazonchallenge.ui.common.BaseViewState
import com.idd.amazonchallenge.ui.common.GenericError
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.actions.GetNetWorkRedditEntriesAction
import com.idd.domain.models.reddit.RedditResponse
import com.idd.domain.models.reddit.RedditResponseDataChildren
import kotlinx.coroutines.launch

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
internal class ListViewModel(
    private val getLocalRedditEntriesAction: GetLocalRedditEntriesAction,
    private val getNetWorkRedditEntriesAction: GetNetWorkRedditEntriesAction
) : BaseViewModel<ListViewModel.ViewState, ListViewModel.Action>(ViewState()) {

    private var redditResponse: RedditResponse? = null
    private var after: String? = null

    override fun onLoadData() {
        if (redditResponse == null) {
            if (BuildConfig.FLAVOR == LOCAL) {
                getLocalRedditEntries()
            } else if (BuildConfig.FLAVOR == NETWORK) {
                getNetWorkRedditEntries()
            }
        } else {
            sendAction(Action.GetRedditSuccess(redditResponse))
        }
    }

    fun getLocalRedditEntries() {
        sendAction(Action.Loading)
        viewModelScope.launch {
            val action = when (val it = getLocalRedditEntriesAction()) {
                is GetLocalRedditEntriesAction.Result.Success -> {
                    redditResponse = it.value
                    markVisitedPosts()
                    Action.GetRedditSuccess(redditResponse)
                }
                GetLocalRedditEntriesAction.Result.Error -> Action.Failure()
                GetLocalRedditEntriesAction.Result.NetworkError -> Action.Failure(errorResourceId = R.string.internet_error)
            }
            sendAction(action)
        }
    }

    fun updateLocalPostStatus(id: String) {
        getLocalRedditEntriesAction.updatePostStatus(id)
    }

    fun getNetWorkRedditEntries() {
        sendAction(Action.Loading)
        viewModelScope.launch {
            val action = when (val it = getNetWorkRedditEntriesAction(PAGE_SIZE, after)) {
                is GetNetWorkRedditEntriesAction.Result.Success -> {
                    redditResponse = it.value
                    after = redditResponse?.redditResponseData?.after
                    markVisitedPosts()
                    Action.GetRedditSuccess(redditResponse)
                }
                is GetNetWorkRedditEntriesAction.Result.Error -> Action.Failure(message = it.value?.description)
                GetNetWorkRedditEntriesAction.Result.NetworkError -> Action.Failure(errorResourceId = R.string.internet_error)
            }
            sendAction(action)
        }
    }

    fun updateNetworkPostStatus(id: String) {
        getNetWorkRedditEntriesAction.updatePostStatus(id)
    }

    private fun markVisitedPosts() {
        if (BuildConfig.FLAVOR == LOCAL) {
            val visitedPostIds = getLocalRedditEntriesAction.getVisitedPosts()
            redditResponse?.redditResponseData?.children?.forEach {
                if (visitedPostIds.contains(it.data.id)) {
                    it.data.readPost = true
                }
            }
        } else if (BuildConfig.FLAVOR == NETWORK) {
            val visitedPostIds = getNetWorkRedditEntriesAction.getVisitedPosts()
            redditResponse?.redditResponseData?.children?.forEach {
                if (visitedPostIds.contains(it.data.id)) {
                    it.data.readPost = true
                }
            }
        }
    }

    fun deletePost(element: RedditResponseDataChildren) {
        (redditResponse?.redditResponseData?.children as ArrayList).remove(element)
    }

    fun deleteAllPost() {
        (redditResponse?.redditResponseData?.children as ArrayList).clear()
    }

    internal data class ViewState(
        val redditEntries: RedditResponse? = null,
        val isLoading: Boolean = false,
        val error: GenericError? = null
    ) : BaseViewState

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.GetRedditSuccess -> state.copy(
            redditEntries = viewAction.redditResponse,
            isLoading = false,
            error = null
        )
        is Action.Failure -> state.copy(
            redditEntries = null,
            isLoading = false,
            error = GenericError(viewAction.message, viewAction.errorResourceId)
        )
        Action.Loading -> state.copy(
            redditEntries = null,
            isLoading = true,
            error = null
        )
    }

    internal sealed class Action : BaseAction {
        data class GetRedditSuccess(val redditResponse: RedditResponse?) :
            Action()

        data class Failure(
            val message: String? = null,
            val errorResourceId: Int = R.string.generic_error
        ) : Action()

        object Loading : Action()
    }

    companion object {
        const val PAGE_SIZE = 50
    }
}

