package com.hara.kaera.feature.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import timber.log.Timber

class GlobalDiffCallBack<T:Any>: DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        Timber.e("[ABC] areItemsTheSame() : ${oldItem.hashCode() == newItem.hashCode()} | old ${oldItem} / new ${newItem}")
        return oldItem.hashCode() == newItem.hashCode()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        Timber.e("[ABC] areContentsTheSame() : ${oldItem == newItem} | old ${oldItem} / new ${newItem}")
        return oldItem == newItem
    }
}