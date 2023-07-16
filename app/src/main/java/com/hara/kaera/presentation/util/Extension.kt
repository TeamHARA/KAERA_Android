package com.hara.kaera.presentation.util

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace

// activity에서 fragment 바꾸기
inline fun <reified T : Fragment> AppCompatActivity.navigateTo(
    @IdRes fragContainerId: Int,
    tag: String? = null,
    action: () -> Unit = {}
) {
    supportFragmentManager.commit {
        replace<T>(fragContainerId, tag)
        action()
        setReorderingAllowed(true)
    }
}