package com.hara.kaera.presentation.util

import android.os.SystemClock
import android.view.View

class SingleClickListener(interval: Int, private val click: (View) -> Unit) :
    View.OnClickListener {

    private val clickInterval = interval
    private var lastClickedTime: Long = 0L

    override fun onClick(v: View?) {
        if (isClickAvailable() && v != null) {
            lastClickedTime = System.currentTimeMillis()
            click(v)
        }
    }

    private fun isClickAvailable(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > clickInterval
    }
}