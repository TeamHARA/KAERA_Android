package com.hara.kaera.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/*
리싸이클러뷰의 하단의 경우 아래 아이템이 안드로이드 자체 내비게이션에 막힐수 있으므로 마지막 아이템에만
별도의 마진을 설정해주어야 하는 경우가 있습니다. 그런경우를 해결하는 코드입니다.
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