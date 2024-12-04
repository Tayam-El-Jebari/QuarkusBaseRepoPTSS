package org.ptss.support.exceptions

import jakarta.xml.bind.ValidationException
import org.ptss.support.enums.ErrorCode

class APIException(
    override val message: String,
    val errorCode: ErrorCode
) : ValidationException(message)