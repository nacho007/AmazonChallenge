package com.idd.amazonchallenge.ui.list

import androidx.lifecycle.viewModelScope
import com.idd.amazonchallenge.BuildConfig
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.constants.LOCAL
import com.idd.amazonchallenge.constants.NETWORK
import com.idd.amazonchallenge.constants.PAGE_SIZE
import com.idd.amazonchallenge.ui.common.BaseAction
import com.idd.amazonchallenge.ui.common.BaseViewModel
import com.idd.amazonchallenge.ui.common.BaseViewState
import com.idd.amazonchallenge.ui.common.GenericError
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.actions.GetNetWorkRedditEntriesAction
import com.idd.domain.models.reddit.RedditResponseDataChildren
import com.idd.domain.models.reddit.RedditResponseDataChildrenData
import kotlinx.coroutines.launch

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
internal class ListViewModel(
    private val getLocalRedditEntriesAction: GetLocalRedditEntriesAction,
    private val getNetWorkRedditEntriesAction: GetNetWorkRedditEntriesAction
) : BaseViewModel<ListViewModel.ViewState, ListViewModel.Action>(ViewState()) {

    private var redditResponse: MutableList<RedditResponseDataChildrenData>? = mutableListOf()
    private var after: String? = null

    override fun onLoadData() {
        if (redditResponse.isNullOrEmpty()) {
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
                    addAll(it.value.redditResponseData.children)
                    after = it.value.redditResponseData.after
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
                    addAll(it.value.redditResponseData.children)
                    after = it.value.redditResponseData.after
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

    private fun addAll(listToAppend: List<RedditResponseDataChildren>?) {
        redditResponse?.addAll(listToAppend?.map { children ->
            RedditResponseDataChildrenData(
                id = children.data.id,
                title = children.data.title,
                author = children.data.author,
                created = children.data.created,
                thumbnail = children.data.thumbnail,
                url = children.data.url,
                numComments = children.data.numComments,
                readPost = false
            )
        }?.toMutableList() ?: mutableListOf())
    }

    private fun markVisitedPosts() {
        val visitedPostIds = when (BuildConfig.FLAVOR) {
            LOCAL -> getLocalRedditEntriesAction.getVisitedPosts()
            NETWORK -> getNetWorkRedditEntriesAction.getVisitedPosts()
            else -> listOf()
        }

        redditResponse?.forEach {
            if (visitedPostIds.contains(it.id)) {
                it.readPost = true
            }
        }
    }

    fun deletePost(element: RedditResponseDataChildrenData) {
        redditResponse?.remove(element)
    }

    fun deleteAllPost() {
        after = ""
        redditResponse?.clear()
    }

    internal data class ViewState(
        val redditEntries: MutableList<RedditResponseDataChildrenData>? = null,
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
        data class GetRedditSuccess(val redditResponse: MutableList<RedditResponseDataChildrenData>?) :
            Action()

        data class Failure(
            val message: String? = null,
            val errorResourceId: Int = R.string.generic_error
        ) : Action()

        object Loading : Action()
    }
}

