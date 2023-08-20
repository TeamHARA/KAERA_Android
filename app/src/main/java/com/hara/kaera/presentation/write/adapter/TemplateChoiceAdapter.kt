package com.hara.kaera.presentation.write.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.ItemTemplateBinding
import com.hara.kaera.presentation.util.GlobalDiffCallBack
import com.hara.kaera.presentation.write.data.TemplateData
import kotlinx.coroutines.selects.select

class TemplateChoiceAdapter(private val itemClickListenr: (Int) -> Unit , private val selectedId: Int) :
    ListAdapter<TemplateData, TemplateChoiceAdapter.ItemViewHolder>(GlobalDiffCallBack()) {

    private lateinit var inflater: LayoutInflater

    /* 선택한 위치의 값을 저장해주는 필드 */
    private var selectedPosition = selectedId

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
        @SuppressLint("RecyclerView") position: Int
    ) {
        val curItem = getItem(position)
        with(holder.binding) {
            /* 준우 킹이 나중에 고쳐줄거얌
            this.templatedata = curItem
            if (position == selectedPosition) { // 선택유무에 따라서 배경이 바뀐다.
                this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_yellow1_8)
                this.select = true // 체크버튼이 나오도록
            } else {
                this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_gray5_8)
                this.select = false
            }

            this.root.setOnClickListener {
                selectedPosition = position
                itemClickListenr(curItem.templateId)
                lastItemSelectedPosition = if (lastItemSelectedPosition == -1) {
                    selectedPosition
                } else {
                    notifyItemChanged(lastItemSelectedPosition)
                    selectedPosition
                }
                notifyItemChanged(selectedPosition)
            }
            */
        }
    }

}