package com.hara.kaera.feature.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


// 홈화면에서 원석/보석간 margin 설정
class GridRvItemIntervalDecoration(
    private val spanCount: Int,
    private val verticalMargin: Int,
    private val horizontalMargin: Int
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount + 1 // 1부터 시작

        /** 첫번째 행(row-1) 이후부터 있는 아이템에만 상단에 space 만큼의 여백을 추가한다. 즉, 첫번째 행에 있는 아이템에는 상단에 여백을 주지 않는다.*/
        // 23/12/16 첫번째 행에 마진을 주지 않을 경우 보석 이펙트가 잘리는 현상이 있어서(패딩값으로 해결 안됨) 첫번째 행도 마진을 주도록 변경 
        outRect.top = verticalMargin
        /** 첫번째 열이 아닌(None Column-1) 아이템들만 좌측에  space 만큼의 여백을 추가한다. 즉, 첫번째 열에 있는 아이템에는 좌측에 여백을 주지 않는다. */
        if (column != 1) {
            outRect.left = horizontalMargin
        }
    }
}