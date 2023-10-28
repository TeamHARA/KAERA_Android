package com.hara.kaera.feature.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.hara.kaera.R
import com.hara.kaera.databinding.ActivityWebviewBinding
import com.hara.kaera.feature.base.BindingActivity
import com.hara.kaera.feature.custom.CustomWebViewClient

class WebViewActivity : BindingActivity<ActivityWebviewBinding>(R.layout.activity_webview) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")


        with(binding.webview) {
            webViewClient = CustomWebViewClient(this.context)
            webChromeClient = WebChromeClient() // 안정성을 위해서 크로미움 클라이언트도 적용
            with(settings) {
                loadsImagesAutomatically = true // 이미지 자동 로드
                javaScriptEnabled = true
                // 만개의 레시피 웹페이자 내부가 javaScript를 통한
                //동작 동적이 있음 따라서 ture로 설정
                allowContentAccess = true
                // content URI 사용을 위함
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                // 쿠팡링크등 Dynamic link 허용을 위함
                cacheMode = WebSettings.LOAD_DEFAULT
            }
        }
        binding.webview.loadUrl(url!!)

        binding.appbarDetail.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            finish()
        }
    }
}
