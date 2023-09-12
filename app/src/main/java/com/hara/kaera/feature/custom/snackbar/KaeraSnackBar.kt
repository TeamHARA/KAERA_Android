package com.hara.kaera.feature.custom.snackbar

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.hara.kaera.R
import com.hara.kaera.databinding.SnackbarLayoutBinding
import com.hara.kaera.feature.util.colorOf

class KaeraSnackBar(view: View, private val message: String, duration: DURATION) {

    companion object {
        fun make(view: View, message: String, duration: DURATION) =
            KaeraSnackBar(view, message, duration)
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
            translationY = (-16.0).toFloat()
            setBackgroundColor(context.colorOf(R.color.transparent))
            addView(binding.root, 0)

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
}