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
        val action = when (val it = savePictureAction(bitmap)) {
            is SavePictureAction.Result.Success -> {
                Action.SavedPictureSuccess(it.value)
            }
            else -> Action.SavedPictureSuccess(false)
        }
        sendAction(action)
    }

    internal data class ViewState(
        val saved: Boolean = false
    ) : BaseViewState

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.SavedPictureSuccess -> state.copy(
            saved = viewAction.saved
        )
    }

    internal sealed class Action : BaseAction {
        data class SavedPictureSuccess(val saved: Boolean) : Action()
    }
}