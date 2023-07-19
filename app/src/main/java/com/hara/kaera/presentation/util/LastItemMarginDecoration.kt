package com.hara.kaera.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
리싸이클러뷰의 하단의 경우
 */
class LastItemMarginItemDecoration(
    private val lastItemMargin: Int
) : RecyclerView.ItemDecoration() {

    private fun isLastItem(parent: RecyclerView, view: View, state: RecyclerView.State): Boolean {
        return parent.getChildAdapterPosition(view) == state.itemCount - 1
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            bottom = if (isLastItem(parent, view, state)) {
                lastItemMargin
            } else {
                0
            }
        }
    }
}