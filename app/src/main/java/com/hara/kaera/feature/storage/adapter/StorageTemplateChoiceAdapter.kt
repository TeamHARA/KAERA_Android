package com.hara.kaera.feature.storage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.R
import com.hara.kaera.databinding.ItemTemplateBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.feature.util.GlobalDiffCallBack

class StorageTemplateChoiceAdapter(
    private val itemClickListener: (Int, String) -> Unit,
    selectedId: Int,
) : ListAdapter<TemplateTypesEntity.Template, StorageTemplateChoiceAdapter.ItemViewHolder>(
    GlobalDiffCallBack(),
) {

    private lateinit var inflater: LayoutInflater

    /* 선택한 위치의 값을 저장해주는 필드 */
    private var selectedPosition = selectedId

    /* 이전에 선택한 위치의 값을 저장해주는 필드*/
    private var lastItemSelectedPosition = 0

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
            if (curItem.templateId == 0) { // '모든 보석 보기'의 경우 보석 이미지 제거
                ivGem.visibility = View.GONE
            } else {
                ivGem.visibility = View.VISIBLE
            }

            if (position == selectedPosition) {
                holder.binding.root.setBackgroundResource(R.drawable.ripple_rect_gray4_10)
                holder.binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        R.color.gray_1,
                    ),
                )
                holder.binding.tvIntroduce.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        R.color.gray_1,
                    ),
                )
            } else { // 배경 및 글자 색상 변경
                holder.binding.root.setBackgroundResource(R.drawable.ripple_rect_gray1_stroke_gray5_8)
                holder.binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        R.color.white,
                    ),
                )
                holder.binding.tvIntroduce.setTextColor(
                    ContextCompat.getColor(
                        holder.binding.root.context,
                        R.color.gray_4,
                    ),
                )
            }

            this.root.setOnClickListener {
                notifyItemChanged(lastItemSelectedPosition)
                selectedPosition = position
                lastItemSelectedPosition = selectedPosition
                notifyItemChanged(selectedPosition)
                itemClickListener(curItem.templateId, curItem.title)
            }
        }
    }
}
