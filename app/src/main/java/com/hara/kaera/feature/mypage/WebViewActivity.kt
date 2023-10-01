package com.hara.kaera.feature.mypage

import android.os.Bundle
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWebviewBinding
import com.hara.kaera.feature.base.BindingActivity

class WebViewActivity : BindingActivity<ActivityWebviewBinding>(R.layout.activity_webview) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        binding.webview.loadUrl(url!!)
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            finish()
        }
    }
}
