package com.hara.kaera.presentation.write.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.ItemTemplateBinding
import com.hara.kaera.presentation.util.GlobalDiffCallBack
import com.hara.kaera.presentation.util.setOnSingleClickListener
import com.hara.kaera.presentation.write.data.TemplateData
import kotlinx.coroutines.selects.select

class TemplateChoiceAdapter() :
    ListAdapter<TemplateData, TemplateChoiceAdapter.ItemViewHolder>(GlobalDiffCallBack()) {

    private lateinit var inflater: LayoutInflater

    /** 선택한 위치의 값을 저장해주는 필드 **/
    private var selectedPosition = -1

    /** 이전에 선택한 위치의 값을 저장해주는 필드**/
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
            this.templatedata = curItem
            if (position == selectedPosition) {
                this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_yellow1_8)
                this.select = true
            } else {
                this.root.setBackgroundResource(R.drawable.shape_rect_gray1_stroke_gray5_8)
                this.select = false
            }
            this.root.setOnClickListener {
                selectedPosition = position
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