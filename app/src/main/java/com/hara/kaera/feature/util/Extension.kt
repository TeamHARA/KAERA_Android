package com.hara.kaera.feature.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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

// 0) 오늘 날짜(고민 시작한 날짜, from day)
// 1) fromDay + days = toDay(고민 끝낼 날짜)
// 를 반환한다
fun getDeadline(days: Int): List<String> {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy.MM.dd")
    val fromDate = Date(System.currentTimeMillis())
    val fromDf = dateFormat.format(fromDate) // [fromDateFormat] 고민기간 中 고민 생성 날짜, 오늘 날짜
    calendar.time = fromDate
    calendar.add(Calendar.DATE, days)
    val toDf = dateFormat.format(calendar.time) // [toDateFormat] 고민기간 中 고민 끝낼 날짜
    return listOf(fromDf, toDf)
}

