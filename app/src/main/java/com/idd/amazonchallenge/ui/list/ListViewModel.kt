package com.idd.amazonchallenge.ui.list

import androidx.lifecycle.viewModelScope
import com.idd.amazonchallenge.R
import com.idd.amazonchallenge.ui.common.BaseAction
import com.idd.amazonchallenge.ui.common.BaseViewModel
import com.idd.amazonchallenge.ui.common.BaseViewState
import com.idd.amazonchallenge.ui.common.GenericError
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.models.RedditResponse
import kotlinx.coroutines.launch

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
internal class ListViewModel(
    private val getLocalRedditEntriesAction: GetLocalRedditEntriesAction
) : BaseViewModel<ListViewModel.ViewState, ListViewModel.Action>(ViewState()) {

    fun getLocalRedditEntries(isRefreshing: Boolean) {
        viewModelScope.launch {
            val action = when (val it = getLocalRedditEntriesAction()) {
                is GetLocalRedditEntriesAction.Result.Success -> {
                    Action.GetRedditSuccess(it.value, isRefreshing)
                }
            }
            sendAction(action)
        }
    }

    fun updateLocalPostStatus(id: String) {
        getLocalRedditEntriesAction.updatePostStatus(id)
    }


    internal data class ViewState(
        val redditEntries: RedditResponse? = null,
        val isRefreshing: Boolean = false,
        val isLoading: Boolean = false,
        val error: GenericError? = null
    ) : BaseViewState

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.GetRedditSuccess -> state.copy(
            redditEntries = viewAction.redditResponse,
            isRefreshing = viewAction.isRefreshing,
            isLoading = false,
            error = null
        )
        is Action.Failure -> state.copy(
            redditEntries = null,
            isRefreshing = false,
            isLoading = false,
            error = GenericError(viewAction.message, viewAction.errorResourceId)
        )
        Action.Loading -> state.copy(
            redditEntries = null,
            isRefreshing = false,
            isLoading = true,
            error = null
        )
    }

    internal sealed class Action : BaseAction {
        data class GetRedditSuccess(val redditResponse: RedditResponse, val isRefreshing: Boolean) :
            Action()

        data class Failure(
            val message: String? = null,
            val errorResourceId: Int = R.string.generic_error
        ) : Action()

        object Loading : Action()
    }
}

