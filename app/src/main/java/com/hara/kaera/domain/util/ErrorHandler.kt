package com.hara.kaera.domain.util

import com.hara.kaera.core.ErrorType


interface ErrorHandler {
    operator fun invoke(throwable: Throwable): ErrorType
}