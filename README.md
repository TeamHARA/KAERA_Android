
![graphic_1024_500](https://github.com/TeamHARA/KAERA_Android/assets/70648111/a79277e7-af82-4976-b739-a784e9f19e9f)

## 프로젝트 소개
나의 고민을 빛나는 보석으로

<aside>
❇️ **고민을 기록하며 나를 탐구하는 시간**
여러분은 스스로에 대해 얼마나 알고 계신가요? 나는 누구인지, 나의 잠재력은 무엇인지, 내가 부족한 점은 무엇인지, 취향에 대한 호불호는 어떠한지 자신있게 설명하실 수 있나요?

캐라에서는 고민을 기록함으로써 나를 찾을 수 있습니다. 
자신의 삶에서 고민이 생겼다는 것은, 더 나은 나를 찾고 싶은 마음에서 생겨난 불안이 있음을 뜻합니다. 기록의 힘을 빌려, 머릿 속이 말끔해지는 기분을 경험해보세요. 캐라를 통해 진정한 자기 자신을 선명하게 그려볼 수 있습니다.

캐라에서는 고민에 대한 부정적인 관점을 바꿔, 반짝이는 나만의 보석을 찾는 과정으로 바라봅니다. 
스스로를 탐구하고 찾는 여정을 느끼고 싶다면, 캐라에서 과거 고민 기록 속의 나와 현재의 나를 연결하며 자신에 대한 확신을 가져보세요.

</aside>

## 출시 링크
[플레이스토어에서 앱 다운로드](https://play.google.com/store/apps/details?id=com.hara.kaera)


## 팀원
| 김준우 [@IslandOfDream](https://github.com/IslandOfDream) | 장유진 [@wkdyujin](https://github.com/wkdyujin) | 이수현 [@skylartosf](https://github.com/skylartosf) |
| :---: | :---: | :---: |
|<img width="1400" src="https://user-images.githubusercontent.com/70648111/210428609-7cc6ae75-c31b-4ae6-9e4f-89437115b3dd.png">|<img width="1400" src="https://avatars.githubusercontent.com/u/69359774?v=4">|<img width="1400" src="https://avatars.githubusercontent.com/u/66207354?v=4">|
<br>

## 기술 스택 
Kotlin기반의 프로젝트이며, Mvvm를 채택하였습니다. 소셜 로그인 도입을 위하여 KaKao Login을 사용하였으며, 그 과정 속 토큰 관리를 위하여 ProtoDataStore를 통한 암호화를 진행하였습니다. 
프로젝트 전반적으로 의존성 주입을 위하여 Hilt/Dagger2를 사용하였으며 크게 Core, Data, Domain, Presentation 을 통한 패키지 관리를 진행하였습니다.
또한, 서버통신에서 Flow를 이용하여 데이터스트림을 구성하였고 일괄적인 에러핸들링을 위해 SealedClass통한 서버통신 결과정의, Mapper, flow 연산자 및 Interceptor등을 활용하였습니다.
반응형 UI를 구현하기 위해서 LivaData가 아닌 Coroutine, StateFlow, 자체 SealedClass를 통한 UI 상태정의 활용해보았습니다.


- Kotlin DSL
- 의존성 주입
    - Hilt / Dagger2
- 서버통신
    - Retrofit2
        - Okhttp3
        - HttpInterceptor
    - Kotlinx Serialization
    - Flow
- Coroutine을 통한 비동기 동작
- Flow를 사용한 서버통신 및 반응형 UI 설계
    - StateFlow
    - AAC ViewModel
    - ListAdapter
- CustomView
    - BottomSheetDialog
    - NumberPicker
    - SnackBar
    - DialogFragment
    - WebView
- KaKao Social login
    - KaKao SDK
- FireBase
    - Firebase Cloud Messaging
    - Firebase Crashlytics
    - Firebase Remote Config
- Local Data
    - ProtoDataStore
    - SharedPreference
- ThirdParty
    - Timber  
    - Lottie
    - skydoves:progressview

## 프로젝트 구조
``` bash
└─com
    └─hara
        └─kaera
            ├─application
            ├─core
            ├─data
            │  ├─datasource
            │  │  ├─local
            │  │  └─remote
            │  ├─dto
            │  │  └─login
            │  ├─mapper
            │  ├─repository
            │  └─util
            ├─di
            ├─domain
            │  ├─dto
            │  ├─entity
            │  │  └─login
            │  ├─repository
            │  ├─usecase
            │  └─util
            └─feature
                ├─base
                ├─custom
                │  └─snackbar
                ├─detail
                │  └─custom
                ├─dialog
                │  └─detail
                ├─home
                │  ├─adapter
                │  └─gems
                ├─login
                ├─mypage
                │  └─custom
                ├─onboarding
                │  └─adpter
                ├─splash
                ├─storage
                │  ├─adapter
                │  ├─viewmodel
                │  └─worrytemplate
                ├─util
                └─write
                    ├─adapter
                    ├─custom
                    └─viewmodel
```



## 스크린샷

![3](https://github.com/TeamHARA/KAERA_Android/assets/70648111/119a06c3-51ee-4f3f-b58c-0673c849fd83)| ![4](https://github.com/TeamHARA/KAERA_Android/assets/70648111/1294ce26-ed41-412f-8322-601a1f6e14c5) | ![5](https://github.com/TeamHARA/KAERA_Android/assets/70648111/ca297a47-0af1-4559-a614-0190f9acf211)
---| ---| ---|

