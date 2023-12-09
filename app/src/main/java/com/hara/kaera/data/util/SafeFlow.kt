package com.hara.kaera.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException

fun <T> Flow<T>.safeCallApi(): Flow<T> {
    return this.flowOn(Dispatchers.IO).catch {
        throw it
    }.retry(3) {
        if (it is HttpException) {
            when (it.code()) {
                401 -> {
                    false
                }

                else -> {
                    delay(1000)
                    true
                }
            }
        } else {
            delay(1000)
            true
        }

    } // exception 발생시 1초 간격으로 최대 3번 재시도 이후 
    // 버튼을 통해 새로고침 하는 방식이면 사라질 코드
    // 401인 경우에는 interceptor를 통해서 토큰 재발급 로직을 수행하므로
    // 재시도하지 않도록 처리
}