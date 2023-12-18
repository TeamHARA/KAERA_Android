package com.hara.kaera.feature.util

import android.content.Context
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.makeSnackBar(message: String) {
    Snackbar.make(
        this,
        message, Snackbar.LENGTH_SHORT
    ).show()
}

fun View.makeToast(message: String) {
    Toast.makeText(
        this.context, message, Toast.LENGTH_SHORT
    ).show()
}

fun View.onSingleClick(clickInterval: Int = 300, click: (View) -> Unit) {
    setOnClickListener(SingleClickListener(clickInterval) { click(it) })
}

fun Context.stringOf(@StringRes resId: Int) = getString(resId)

fun Context.colorOf(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.drawableOf(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

fun Fragment.stringOf(@StringRes resId: Int) = getString(resId)

fun Fragment.colorOf(@ColorRes resId: Int) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawableOf(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(requireContext(), resId)


fun Int.dpToPx(context: Context): Int {
    return context.resources.displayMetrics.density.let { density ->
        (this * density).toInt()
    }
}

fun Int.pxToDp(context: Context): Int {
    return context.resources.displayMetrics.density.let { density ->
        (this / density).toInt()
    }
}

fun Int.dpToSp(context: Context): Int {
    return context.resources.displayMetrics.scaledDensity.let { density ->
        (this * density).toInt()
    }
}

fun Int.spToDp(context: Context): Int {
    return context.resources.displayMetrics.scaledDensity.let { density ->
        (this / density).toInt()
    }
}

fun ImageButton.increaseTouchSize(expandSize: Int = 24, context: Context) {
    val addHitSize = expandSize.dpToPx(context) // 추가로 Hit하려는 크기
    val targetView = this
    val parentView = targetView.parent as View

    parentView.doOnPreDraw { parent ->
        val updateHitRect = Rect().also { r ->
            targetView.getHitRect(r)
            r.inset(-addHitSize, -addHitSize)
        }
        parent.touchDelegate = TouchDelegate(updateHitRect, targetView) // <-- 핵심 코드
    }
}

