package com.hara.kaera.feature.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.hara.kaera.core.ErrorType

// activity에서 fragment 바꾸기
inline fun <reified T : Fragment> AppCompatActivity.navigateTo(
    @IdRes fragContainerId: Int, tag: String? = null, action: () -> Unit = {}
) {
    supportFragmentManager.commit {
        replace<T>(fragContainerId, tag)
        action()
        setReorderingAllowed(true)
    }
}

fun errorToMessage(error: ErrorType) = when (error) {
    is ErrorType.Api.BadRequest -> "잘못된 응답입니다."
    is ErrorType.Api.UnAuthorized -> "인증되지 않은 사용입니다."
    is ErrorType.Api.Forbidden -> "잘못된 접근 입니다."
    is ErrorType.Api.ResourceNotFound -> "유효하지 않은 접근입니다."
    is ErrorType.Api.MethodNotAllowed -> "유효하지 않은 호출입니다."
    is ErrorType.Api.InternalServer -> "잠시후 다시 시도해주세요."
    is ErrorType.Api.ServiceUnavailable -> "서버가 불안정합니다. 잠시후 다시 시도해주세요."
    is ErrorType.Api.GateWayTimeOut -> "잠시후 다시 시도해주세요."
    is ErrorType.Network -> "인터넷 연결이 되어있지 않습니다."
    is ErrorType.Socket -> "인터넷 연결이 불안정합니다."
    is ErrorType.IoException -> "알수없는 오류입니다."
    is ErrorType.Unknown -> "알수없는 오류입니다."
}

/*
앱/웹 에러 혹은 인터넷 에러 레이아웃로 대체되어야 하는 화면의 경우 해당 함수 사용
 */
fun errorToLayout(error: ErrorType) = when(error){
    is ErrorType.Api -> Constant.networkError
    else -> Constant.internalError
}
