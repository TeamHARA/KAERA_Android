package com.hara.kaera.presentation.util

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.hara.kaera.R

/*
    템플릿 아이디와 이전에 사용한적 있는지에 따라서 이미지를 변화시켜주는 함수
 */
@BindingAdapter(value = ["app:templateId", "app:templateHasUsed"], requireAll = true)
fun ImageView.setGemStoneSrc(templateId: Int, hasUsed: Boolean) {
    this.apply {
        if (hasUsed) {
            when (templateId) {
                1 -> {setImageResource(R.drawable.gem_blue_s_on)}
                2 -> {setImageResource(R.drawable.gem_red_s_on)}
                3 -> {setImageResource(R.drawable.gem_orange_s_on)}
                4 -> {setImageResource(R.drawable.gem_green_s_on)}
                5 -> {setImageResource(R.drawable.gem_pink_s_on)}
                6 -> {setImageResource(R.drawable.gem_yellow_s_on)}
                else -> {setImageResource(R.drawable.icn_warningrt)}
            }
        } else {
            when (templateId) {
                1 -> {this.setImageResource(R.drawable.gem_blue_s_off)}
                2 -> {setImageResource(R.drawable.gem_red_s_off)}
                3 -> {setImageResource(R.drawable.gem_orange_s_off)}
                4 -> {setImageResource(R.drawable.gem_green_s_off)}
                5 -> {setImageResource(R.drawable.gem_pink_s_off)}
                6 -> {setImageResource(R.drawable.gem_yellow_s_off)}
                else -> {setImageResource(R.drawable.icn_warningrt)}
            }
        }
    }

}

// [홈화면] templateId & isSolved(0/1) 별로 이미지 매핑
//@BindingAdapter(value = ["app:templateId", "app:isSolved"], requireAll = true)
//fun ImageView.setGemStoneSrc(templateId: Int, isSolved: Int) {
//    this.apply {
//        if (isSolved == 1) { // 보석
//            when (templateId) {
//                1 -> {setImageResource(R.drawable.gem_blue_s_on)}
//                2 -> {setImageResource(R.drawable.gem_red_s_on)}
//                3 -> {setImageResource(R.drawable.gem_orange_s_on)}
//                4 -> {setImageResource(R.drawable.gem_green_s_on)}
//                5 -> {setImageResource(R.drawable.gem_pink_s_on)}
//                6 -> {setImageResource(R.drawable.gem_yellow_s_on)}
//                else -> {setImageResource(R.drawable.icn_warningrt)}
//            }
//        } else { // 원석
//            when (templateId) {
//                1 -> {this.setImageResource(R.drawable.gem_blue_s_off)}
//                2 -> {setImageResource(R.drawable.gem_red_s_off)}
//                3 -> {setImageResource(R.drawable.gem_orange_s_off)}
//                4 -> {setImageResource(R.drawable.gem_green_s_off)}
//                5 -> {setImageResource(R.drawable.gem_pink_s_off)}
//                6 -> {setImageResource(R.drawable.gem_yellow_s_off)}
//                else -> {setImageResource(R.drawable.icn_warningrt)}
//            }
//        }
//    }
//}

@BindingAdapter("app:completebutton")
fun AppCompatButton.setBackground(activate: Boolean){
    if(activate){
        this.background = this.context.getDrawable(R.drawable.shape_rect_yellow1_10)
        this.setTextColor(this.context.getColor(R.color.black))
    }else{
        this.background = this.context.getDrawable(R.drawable.shape_rect_gray4_10)
        this.setTextColor(this.context.getColor(R.color.white))
    }
}