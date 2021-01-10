package com.idd.domain.models

/**
 * Created by ignaciodeandreisdenis on 1/8/21.
 */
data class ErrorResponse(
    val error: String,
    val description: String? = null
)
