package com.idd.infrastructure.network

import com.google.gson.Gson
import com.idd.domain.models.ErrorResponse
import com.idd.domain.models.ResultWrapper
import com.idd.infrastructure.entities.ErrorResponseEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by ignaciodeandreisdenis on 1/8/21.
 */
class ResponseHandler(
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun <T : Any> invoke(apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        ResultWrapper.NetworkError
                    }
                    is HttpException -> {
                        val code = throwable.code()
                        val errorBody = getErrorBody(throwable)
                        ResultWrapper.Error(null, null)
                        val errorResponse = convertToErrorResponse(errorBody)

                        errorResponse.let {
                            ResultWrapper.Error(code, errorResponse)
                        }
                    }
                    else -> {
                        ResultWrapper.Error(null, null)
                    }
                }
            } catch (ex: Exception) {
                ResultWrapper.Error(null, null)
            }
        }
    }

    private fun convertToErrorResponse(errorBody: String): ErrorResponse {
        var errorResponse = ErrorResponse("Generic error", null)
        try {
            errorBody.let {
                val adapter = Gson().getAdapter(ErrorResponseEntity::class.java)
                val errorResponseEntity = adapter.fromJson(it)

                errorResponse = errorResponseEntity.toErrorResponse()
            }
        } catch (exception: Exception) {

        }

        return errorResponse
    }

    private fun getErrorBody(throwable: HttpException): String {
        return throwable.response()?.errorBody()?.string() ?: ""
    }

}
