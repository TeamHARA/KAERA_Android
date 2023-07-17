package com.hara.kaera.presentation.write

import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWriteBinding
import com.hara.kaera.presentation.base.BindingActivity

class WriteActivity : BindingActivity<ActivityWriteBinding>(R.layout.activity_write) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
    }

}