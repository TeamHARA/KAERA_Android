package com.hara.kaera.core

object ErrorCodes {

    object Client {
        const val BadRequest = 400
        const val UnAuthorized = 401
        const val Forbidden = 403
        const val ResourceNotFound = 404
        const val MethodNotAllowed = 405
    }
    object Server{
        const val InternalServer = 500
        const val ServiceUnavailable = 503
        const val GateWayTimeOut = 504
    }
}