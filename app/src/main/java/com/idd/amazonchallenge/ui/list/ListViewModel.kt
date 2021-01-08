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

    override fun onLoadData() {
        if (redditResponse == null) {
            if (BuildConfig.PRODUCT_FLAVOUR == LOCAL) {
                getLocalRedditEntries()
            } else if (BuildConfig.PRODUCT_FLAVOUR == NETWORK) {
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
            val action = when (val it = getNetWorkRedditEntriesAction()) {
                is GetNetWorkRedditEntriesAction.Result.Success -> {
                    redditResponse = it.value
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

    fun deletePost(element: RedditResponseDataChildren) {
        (redditResponse?.redditResponseData?.children as ArrayList).remove(element)
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
}

