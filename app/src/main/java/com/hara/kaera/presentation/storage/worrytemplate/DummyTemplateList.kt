package com.hara.kaera.presentation.storage.worrytemplate

data class DummyTemplateList(
    val `data`: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean,
) {
    data class Data(
        val hasUsed: Boolean,
        val info: String,
        val shortInfo: String,
        val templateId: Int,
        val title: String,
        var expand: Boolean = false,
    )
}

val dummyData = listOf(
    DummyTemplateList.Data(
        templateId = 1,
        title = "Free Note",
        shortInfo = "빈 공간을 자유롭게 채우기",
        info = "어떤 질문도 던지지 않아요.\n캐라 도화지에서 머릿 속 얽혀있는 고민 실타래들을 마음껏 풀어내세요!",
        hasUsed = false,
    ),
    DummyTemplateList.Data(
        templateId = 2,
        title = "장단점 생각하기",
        shortInfo = "할까?말까? 최고의 선택을 돕는 고민 해결사",
        info = "내가 할 수 있는 선택지를 나열해보세요.\n각각 어떤 장점과 단점을 가지고 있나요? 당신의 가능성을 펼쳐 비교해 더 나은 선택을 할 수 있도록 캐라가 함께할게요.",
        hasUsed = false,
    ),
    DummyTemplateList.Data(
        templateId = 6,
        title = "가치관 탐구하기",
        shortInfo = "삶의 중요 가치를 통해 결정하기",
        info = "인생의 방향이 혼란스러우신가요?\n내가 가장 중요시 여기는 1순위의 가치관에 맞춰 결정해보세요. 내가 원하는 진정한 가치를 쫓아 후회없는 선택을 향해 나아갈 수 있도록 도와줄게요.",
        hasUsed = false,
    ),
)
