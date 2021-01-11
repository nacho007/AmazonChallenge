package com.idd.domain.actions

import com.idd.domain.repositories.SavePictureRepository

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
class SavePictureAction(private val savePicture: SavePictureRepository) {

    sealed class Result {
        object Success : Result()
        object Error : Result()
    }

    operator fun <T> invoke(param: T): Result {
        return if (savePicture.savePicture(param)) {
            Result.Success
        } else {
            Result.Error
        }
    }
}

