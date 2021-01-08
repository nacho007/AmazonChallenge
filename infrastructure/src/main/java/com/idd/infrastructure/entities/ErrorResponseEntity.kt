package com.idd.infrastructure.entities

import com.google.gson.annotations.SerializedName
import com.idd.domain.models.ErrorResponse

/**
 * Created by ignaciodeandreisdenis on 1/8/21.
 */
data class ErrorResponseEntity(
    @SerializedName("error") val error: String,
    @SerializedName("description") val description: String
) {
    fun toErrorResponse(): ErrorResponse {
        return ErrorResponse(error, description)
    }
}