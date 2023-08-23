package com.hara.kaera.domain.util

import com.hara.kaera.data.util.ErrorType


interface ErrorHandler {
    operator fun invoke(throwable: Throwable): ErrorType
}