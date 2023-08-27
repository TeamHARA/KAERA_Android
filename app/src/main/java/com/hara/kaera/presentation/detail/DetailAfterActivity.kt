package com.hara.kaera.presentation.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityDetailAfterBinding
import com.hara.kaera.domain.entity.WorryDetailEntity
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAfterActivity :
    BindingActivity<ActivityDetailAfterBinding>(R.layout.activity_detail_after) {
    private val viewModel by viewModels<DetailAfterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val worryId = intent.getIntExtra("worryId", 0)
        viewModel.getWorryDetail(worryId)
    }

    private fun render(uiState: UiState<WorryDetailEntity>) {
        when (uiState) {
            is UiState.Init -> Unit
            is UiState.Loading -> Unit
            is UiState.Success<WorryDetailEntity> -> {
                val worryDetail = uiState.data
            }
            is UiState.Error -> Unit
        }
    }
}
