package com.hara.kaera.presentation.write.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.ItemTemplateBinding
import com.hara.kaera.presentation.util.GlobalDiffCallBack
import com.hara.kaera.presentation.write.Mode
import com.hara.kaera.presentation.write.data.TemplateData

class TemplateChoiceAdapter(private val itemClickListener: (Int) -> Unit, private val mode: Mode) :
    ListAdapter<TemplateData, TemplateChoiceAdapter.ItemViewHolder>(GlobalDiffCallBack()) {

    private lateinit var inflater: LayoutInflater

    /* 선택한 위치의 값을 저장해주는 필드 */
    private var selectedPosition = -1

    /* 이전에 선택한 위치의 값을 저장해주는 필드*/
    private var lastItemSelectedPosition = -1

    class ItemViewHolder(val binding: ItemTemplateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ItemViewHolder(ItemTemplateBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        val curItem = getItem(position)
        with(holder.binding) {
            this.templatedata = curItem
            when (mode) {
                Mode.WRITE -> { // 글쓰기에서 호출한 경우의 동작
                    // TODO: templateId == 0 변경
                    if (curItem.templateId == -1) {
                        this.root.visibility = View.GONE
                    } else {
                        this.root.visibility = View.VISIBLE
                    }

                    if (position == selectedPosition) { // 선택유무에 따라서 배경이 바뀐다.
                        this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_yellow1_8)
                        this.select = true // 체크버튼이 나오도록
                    } else {
                        this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_gray5_8)
                        this.select = false
                    }
                }

                Mode.STORAGE -> { // 보관함에서 호출한 경우의 동작
                    if (curItem.templateId == -1) { // '모든 보석 보기'의 경우 보석 이미지 제거
                        ivGem.visibility = View.GONE
                    } else {
                        ivGem.visibility = View.VISIBLE
                    }

                    if (position == selectedPosition) {
                        holder.binding.root.setBackgroundResource(R.drawable.shape_rect_gray4_10)
                        holder.binding.tvTitle.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.gray_1))
                        holder.binding.tvIntroduce.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.gray_1))
                    } else { // 배경 및 글자 색상 변경
                        holder.binding.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_gray5_8)
                        holder.binding.tvTitle.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.white))
                        holder.binding.tvIntroduce.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.gray_4))
                    }
                }
            }

            this.root.setOnClickListener {
                selectedPosition = position
                itemClickListener(curItem.templateId)
                lastItemSelectedPosition = if (lastItemSelectedPosition == -1) {
                    selectedPosition
                } else {
                    notifyItemChanged(lastItemSelectedPosition)
                    selectedPosition
                }
                notifyItemChanged(selectedPosition)
            }
        }
    }
}
