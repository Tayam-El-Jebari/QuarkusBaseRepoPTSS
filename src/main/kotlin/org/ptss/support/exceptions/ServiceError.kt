package org.ptss.support.exceptions

import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "Error response")
data class ServiceError(
    val status: Int,
    val message: String,
    val errorCode: String
)
