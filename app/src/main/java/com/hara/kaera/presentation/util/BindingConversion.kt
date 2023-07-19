package com.hara.kaera.presentation.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.hara.kaera.R

/*
    템플릿 아이디와 이전에 사용한적 있는지에 따라서 이미지를 변화시켜주는 함수
 */
@BindingAdapter(value = ["bind:templateId", "bind:templateHasUsed"], requireAll = true)
fun ImageView.setGemStoneSrc(templateId: Int, hasUsed: Boolean) {
    this.apply {
        if (hasUsed) {
            when (templateId) {
                0 -> {setImageResource(R.drawable.gem_orange_s_on)}
                1 -> {setImageResource(R.drawable.gem_pink_s_on)}
                2 -> {setImageResource(R.drawable.gem_red_s_on)}
                3 -> {setImageResource(R.drawable.icn_warningrt)}
                4 -> {setImageResource(R.drawable.gem_blue_s_on)}
                5 -> {setImageResource(R.drawable.gem_green_s_on)}
                6 -> {setImageResource(R.drawable.gem_yellow_s_on)}
            }
        } else {
            when (templateId) {
                0 -> {this.setImageResource(R.drawable.gem_orange_s_off)}
                1 -> {setImageResource(R.drawable.gem_pink_s_off)}
                2 -> {setImageResource(R.drawable.gem_red_s_off)}
                3 -> {setImageResource(R.drawable.icn_warningrt)}
                4 -> {setImageResource(R.drawable.gem_blue_s_off)}
                5 -> {setImageResource(R.drawable.gem_green_s_off)}
                6 -> {setImageResource(R.drawable.gem_yellow_s_off)}
            }
        }
    }



}