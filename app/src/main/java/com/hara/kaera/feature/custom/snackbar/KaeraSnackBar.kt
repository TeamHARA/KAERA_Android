package com.hara.kaera.feature.custom.snackbar

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.hara.kaera.R
import com.hara.kaera.databinding.SnackbarLayoutBinding
import com.hara.kaera.feature.util.colorOf

class KaeraSnackBar(
    view: View,
    private val message: String,
    duration: DURATION,
    private val backgroundColor: BACKGROUNDCOLOR,
    private val locationY: Double,
) {

    companion object {
        /*
        기본값으로 스낵바의 배경색은 GRAY2입니다.
        GRAY5를 원하면 파라미터로 GRAY5를 넘겨주면 됩니다.
        위치값은 기본적으로 -16.0 이며 더 중앙에 배치하고 싶다면 더 낮은 값 ex)-100.0
        을 넘겨주면 됩니다.
         */
        fun make(
            view: View,
            message: String,
            duration: DURATION,
            backgroundColor: BACKGROUNDCOLOR = BACKGROUNDCOLOR.GRAY_2,
            locationY: Double = -16.0,
        ) =
            KaeraSnackBar(view, message, duration, backgroundColor, locationY)
    }

    private val context = view.context
    private val snackBar =
        when (duration) {
            DURATION.SHORT -> {
                Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
            }

            DURATION.LONG -> {
                Snackbar.make(view, "", Snackbar.LENGTH_LONG)
            }
        }

    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    private val inflater = LayoutInflater.from(context)
    private val binding: SnackbarLayoutBinding =
        DataBindingUtil.inflate(inflater, R.layout.snackbar_layout, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {

        with(snackBarLayout) {
            removeAllViews()
            translationY = (locationY).toFloat()
            setBackgroundColor(context.colorOf(R.color.transparent))
            addView(binding.root, 0)
            if (backgroundColor == BACKGROUNDCOLOR.GRAY_5) {
                binding.clSnackbar.setBackgroundResource(R.drawable.shape_rect_gray5_4)
                binding.tvMessage.setTextColor(context.getColor(R.color.gray_1))
            }
        }
    }

    private fun initData() {
        binding.text = message
    }

    fun show() {
        snackBar.show()
    }

    enum class DURATION(val duration: Int) {
        LONG(1),
        SHORT(-1)
    }

    enum class BACKGROUNDCOLOR {
        GRAY_2,
        GRAY_5
    }
}