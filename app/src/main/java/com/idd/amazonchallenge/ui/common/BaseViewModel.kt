package com.idd.amazonchallenge.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
abstract class BaseViewModel<ViewState : BaseViewState, ViewAction : BaseAction>(initialState: ViewState) :
    ViewModel() {

    private val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.asLiveData()

    // Delegate will handle state event deduplication
    // (multiple states of the same type holding the same data will not be dispatched multiple times to LiveData stream)
    protected var state by Delegates.observable(initialState) { _, _, new ->
        stateMutableLiveData.value = new
    }

    fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }

    fun loadData() {
        onLoadData()
    }

    protected open fun onLoadData() {}

    protected abstract fun onReduceState(viewAction: ViewAction): ViewState
}

interface BaseViewState
interface BaseAction

fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

data class GenericError(val message: String? = null, val errorResourceId: Int)
