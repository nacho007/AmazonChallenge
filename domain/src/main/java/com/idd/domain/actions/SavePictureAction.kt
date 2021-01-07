package com.idd.domain.actions

import com.idd.domain.repositories.SavePictureRepository

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class SavePictureAction(private val savePicture: SavePictureRepository) {

    sealed class Result {
        data class Success(val value: Boolean) : Result()
    }

    operator fun <T> invoke(param: T): Result {
        return Result.Success(savePicture.savePicture(param))
    }
}
