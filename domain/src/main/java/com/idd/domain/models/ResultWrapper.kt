package com.idd.domain.models

/**
 * Created by ignaciodeandreisdenis on 1/7/21.
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: Int? = null) :
        ResultWrapper<Nothing>()
}
