package com.hara.kaera.presentation.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

fun View.makeSnackbar(messgae: String) {
    Snackbar.make(
        this,
        messgae, Snackbar.LENGTH_SHORT
    ).show()
}

fun View.makeToast(messgae: String) {
    Toast.makeText(
        this.context, messgae, Toast.LENGTH_SHORT
    ).show()
}

fun View.onSingleClick(clickInterval: Int, click: (View) -> Unit) {
    setOnClickListener(SingleClickListener(clickInterval) { click(it) })
}

fun Context.stringOf(@StringRes resId: Int) = getString(resId)

fun Context.colorOf(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.drawableOf(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)

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