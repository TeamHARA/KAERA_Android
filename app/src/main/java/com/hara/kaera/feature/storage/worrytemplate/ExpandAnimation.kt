package com.hara.kaera.feature.storage.worrytemplate

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

class ExpandAnimation {

    companion object {

        fun toggleArrow(view: View, isExpanded: Boolean): Boolean {
            return if (!isExpanded) {
                view.animate().setDuration(200).rotation(0f)
                false
            } else {
                view.animate().setDuration(200).rotation(180f)
                true
            }
        }

        fun expand(view: View) {
            val animation = expandAction(view)
            view.startAnimation(animation)
        }

        @SuppressLint("Range")
        private fun expandAction(view: View): Animation {
            view.measure(
                View.MeasureSpec.makeMeasureSpec(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    View.MeasureSpec.EXACTLY,
                ),
                View.MeasureSpec.makeMeasureSpec(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    View.MeasureSpec.AT_MOST,
                ),
            )
            // 정밀도를 따로 지정해주어서 정확하게 확장될 view의 높이를 정확하게 측정
            var actualHeight = view.measuredHeight

            view.layoutParams.height = 0
            view.visibility = View.VISIBLE

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    view.layoutParams.height = if (interpolatedTime == 1f) {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    } else {
                        (actualHeight * interpolatedTime).toInt()
                    }
                    view.requestLayout()
                }
            }
            animation.duration =
                (actualHeight / view.context.resources.displayMetrics.density).toLong()

            view.startAnimation(animation)

            return animation
        }

        fun collapse(view: View) {
            val actualHeight = view.measuredHeight

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    if (interpolatedTime == 1f) {
                        view.visibility = View.GONE
                        view.requestLayout()
                    } else {
                        view.layoutParams.height =
                            (actualHeight - (actualHeight * interpolatedTime)).toInt()
                        view.requestLayout()
                    }
                }
            }

            animation.duration =
                (actualHeight / view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)
        }
    }
}
