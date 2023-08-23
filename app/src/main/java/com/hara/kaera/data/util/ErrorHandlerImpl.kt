package com.hara.kaera.data.util

import com.hara.kaera.domain.util.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException

class ErrorHandlerImpl : ErrorHandler {

    override operator fun invoke(throwable: Throwable) = when (throwable) {
        is ConnectException -> ErrorType.Network
        is SocketException -> ErrorType.Socket
        is HttpException -> when (throwable.code()) {
            ErrorCodes.Client.BadRequest -> ErrorType.Api.BadRequest
            ErrorCodes.Client.UnAuthorized -> ErrorType.Api.UnAuthorized
            ErrorCodes.Client.Forbidden -> ErrorType.Api.Forbidden
            ErrorCodes.Client.ResourceNotFound -> ErrorType.Api.ResourceNotFound
            ErrorCodes.Client.MethodNotAllowed -> ErrorType.Api.MethodNotAllowed
            ErrorCodes.Server.InternalServer -> ErrorType.Api.InternalServer
            ErrorCodes.Server.ServiceUnavailable -> ErrorType.Api.ServiceUnavailable
            ErrorCodes.Server.GateWayTimeOut -> ErrorType.Api.GateWayTimeOut
            else -> {
                ErrorType.Unknown
            }
        }

        is IOException -> ErrorType.IoException
        else -> ErrorType.Unknown
    }
}
