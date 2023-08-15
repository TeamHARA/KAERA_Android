package com.hara.kaera.presentation.util

/*
클래스 이름 처럼 Ui의 상태를 나타내주는 실드클래스
기존에는 라이브데이터 여러개를 이용해서 사용했다면 다음과 같이
Ui의 상태를 나타내는 오브젝트/ 클래스등을 미리 선언해두고 flow와 연계하여서
현재 Ui가 로딩인지 성공인지 실패인지 판별한다.
 */

sealed class UiState<out T> {   // Use Sealed Class
    object Init : UiState<Nothing>()
    //초기상태
    object Loading : UiState<Nothing>()
    //로딩상태
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
}