package com.hara.kaera.presentation.storage.data

import com.hara.kaera.presentation.write.data.TemplateData

object DummyStorageTemplateData {
    val templateList = listOf<TemplateData>(
        TemplateData(0, "모든 보석 보기", "그동안 캐낸 모든 보석을 볼 수 있어요", false),
        TemplateData(1, "Free flow", "빈 공간 자유롭게 채우기", false),
        TemplateData(2, "장단점 생각하기", "고민의 장단점을 비교하며 깊게 생각해보세요.", false),
        TemplateData(3, "세번의 왜?", "내 고민 속 물음의 꼬리를 통해 해결책을 찾아가요.", true),
        TemplateData(4, "단 하나의 목표", "내 고민의 우선순위를 통해 생각을 정리해보세요.", false),
        TemplateData(5, "땡스투 새겨보기", "부정을 긍정으로, 사고를 전환해보세요.", false),
        TemplateData(6, "10 - 10 - 10", "시간을 기준으로 내 고민의 미래를 생각해보세요.", true),
        TemplateData(7, "걱정을 극복하는 <자기관리론>", "데일 카네기가 제시한 방법에 따라 걱정을 극복해보세요.", false),
    )
}