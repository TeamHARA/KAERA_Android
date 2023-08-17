package com.hara.kaera.presentation.storage.worrytemplate

import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWorryTemplateBinding
import com.hara.kaera.presentation.base.BindingActivity

class WorryTemplateActivity : BindingActivity<ActivityWorryTemplateBinding>(R.layout.activity_worry_template) {
    private lateinit var worryTemplateAdapter: WorryTemplateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        worryTemplateAdapter = WorryTemplateAdapter()
        binding.rcvWorryTemplate.adapter = worryTemplateAdapter
        worryTemplateAdapter.submitList(dummyData)
    }
}
