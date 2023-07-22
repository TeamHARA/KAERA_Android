package com.hara.kaera.presentation.write

import android.os.Bundle
import android.view.View
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.presentation.base.BindingActivity
import com.hara.kaera.presentation.util.setOnSingleClickListener
import timber.log.Timber

class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.clEmpty.visibility = View.VISIBLE
        //binding.clEmpty.visibility = View.INVISIBLE
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.apply {
            appbarDetail.setNavigationOnClickListener {
                finish()
            }
            clChoice.setOnSingleClickListener {
                Timber.e("바텀시트 올리기")
            }
        }
    }

}