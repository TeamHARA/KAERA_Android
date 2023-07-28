package com.hara.kaera.presentation.write

/*
클래스 이름 처럼 Ui의 상태를 나타내주는 실드클래스
기존에는 라이브데이터 여러개를 이용해서 사용했다면 다음과 같이
Ui의 상태를 나타내는 오브젝트/ 클래스등을 미리 선언해두고 flow와 연계하여서
현재 Ui가 로딩인지 성공인지 실패인지 판별한다.
 */

sealed class UiState {
    object Loading : UiState()
    // 로딩이니까 담아줄꺼 없으므로 그냥 오브젝트
    data class Success<out T>(val data:T) : UiState()
    // 제네릭 타입으로 아무 타입이나 담을수 있도록 즉, 서버통신 결과가 여기에 담길것
    data class Error(val message:String) : UiState()
    // String으로 에러메시지 담도록 의도 T 타입해서 다양한 상호작용 가능
}