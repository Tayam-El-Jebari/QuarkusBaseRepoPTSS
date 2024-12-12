package org.ptss.support.common.exceptions

import jakarta.xml.bind.ValidationException
import org.ptss.support.domain.enums.ErrorCode

class APIException(
    override val message: String,
    val errorCode: ErrorCode,
    val details: ErrorDetails? = null
) : ValidationException(message)