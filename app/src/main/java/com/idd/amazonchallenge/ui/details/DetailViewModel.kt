package com.idd.amazonchallenge.ui.details

import android.graphics.Bitmap
import com.idd.amazonchallenge.ui.common.BaseAction
import com.idd.amazonchallenge.ui.common.BaseViewModel
import com.idd.amazonchallenge.ui.common.BaseViewState
import com.idd.domain.actions.SavePictureAction

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
internal class DetailViewModel(
    private val savePictureAction: SavePictureAction
) : BaseViewModel<DetailViewModel.ViewState, DetailViewModel.Action>(ViewState()) {

    fun savePicture(bitmap: Bitmap?) {
        val action = when (savePictureAction(bitmap)) {
            is SavePictureAction.Result.Success -> {
                Action.SavedPictureSuccess
            }
            else -> Action.SavedPictureError
        }
        sendAction(action)
    }

    internal data class ViewState(
        val saved: Boolean = false
    ) : BaseViewState

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.SavedPictureSuccess -> state.copy(
            saved = true
        )
        is Action.SavedPictureError -> state.copy(
            saved = false
        )
    }

    internal sealed class Action : BaseAction {
        object SavedPictureSuccess : Action()
        object SavedPictureError : Action()
    }
}