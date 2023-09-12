package com.hara.kaera.feature.home

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object FloatingAnimation {

    fun floatingAnimationWithValueAnimator(
        start: Int,
        dur: Long,
        view: View,
        movement: Float
    ): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            duration = dur
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = start.toLong()
            addUpdateListener {
                view.translationY = animatedValue as Float * movement
            }
        }
    }

}