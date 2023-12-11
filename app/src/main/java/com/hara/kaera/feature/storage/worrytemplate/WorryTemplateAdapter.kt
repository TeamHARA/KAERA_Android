package com.hara.kaera.feature.storage.worrytemplate

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hara.kaera.databinding.ItemWorryTemplateBinding
import com.hara.kaera.databinding.LayoutWorryTemplateBannerBinding
import com.hara.kaera.domain.entity.TemplateTypesEntity
import com.hara.kaera.feature.mypage.MypageActivity
import com.hara.kaera.feature.mypage.WebViewActivity
import com.hara.kaera.feature.util.GlobalDiffCallBack
import timber.log.Timber

class WorryTemplateAdapter(private val itemClickListener: (Int) -> Unit) :
    ListAdapter<TemplateTypesEntity.Template, RecyclerView.ViewHolder>(
        GlobalDiffCallBack(),
    ) {
    private lateinit var inflater: LayoutInflater

    class BannerViewHolder(val binding: LayoutWorryTemplateBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemViewHolder(val binding: ItemWorryTemplateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_BANNER else TYPE_TEMPLATE
    }

    override fun getItemCount() = super.getItemCount() + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(
                LayoutWorryTemplateBannerBinding.inflate(
                    inflater,
                    parent,
                    false,
                ),
            )

            else -> ItemViewHolder(ItemWorryTemplateBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> {
                holder.binding.ibConnectInstagramBtn.setOnClickListener {
                    holder.itemView.context.startActivity(
                        Intent(
                            holder.itemView.context, WebViewActivity::class.java
                        ).apply {
                            putExtra(
                                MypageActivity.WebViewConstant.urlIntent,
                                MypageActivity.WebViewConstant.useOfTerms
                            )
                        }
                    )
                }
            }

            is ItemViewHolder -> {
                val curItem = getItem(position - 1)
                with(holder.binding) {
                    this.template = curItem

                    this.clTemplateTitle.setOnClickListener {
                        val currentState = this.isExpand ?: false
                        ExpandAnimation.toggleArrow(ivArrow, !currentState)
                        this.isExpand = !currentState
                    }

                    this.btnWrite.setOnClickListener {
                        itemClickListener(curItem.templateId)
                    }
                }
            }
        }
    }

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_TEMPLATE = 1
    }
}
