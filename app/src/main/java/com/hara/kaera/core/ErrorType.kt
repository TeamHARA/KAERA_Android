package com.hara.kaera.core

sealed class ErrorType {

    sealed class Api : ErrorType() {
        object BadRequest : Api()
        object UnAuthorized : Api()
        object Forbidden : Api()
        object ResourceNotFound : Api()
        object MethodNotAllowed : Api()
        object InternalServer : Api()
        object ServiceUnavailable : Api()
        object GateWayTimeOut : Api()

    }

    object IoException : ErrorType()
    object Network : ErrorType()
    object Socket : ErrorType()
    object Unknown : ErrorType()
}