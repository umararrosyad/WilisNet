package com.example.wilisnet.activity.user

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.wilisnet.R
import com.example.wilisnet.databinding.ActivityVoucherBinding
import com.example.wilisnet.databinding.ActivityWebBinding
import com.example.wilisnet.databinding.ActivityWelcomeBinding

class WebActivity : AppCompatActivity() {
    private lateinit var b : ActivityWebBinding
    private lateinit var webView: WebView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWebBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        webView = b.webView
        webView.webViewClient = WebViewClient()

        // Aktifkan JavaScript jika diperlukan
        webView.settings.javaScriptEnabled = true

        // Load URL ke WebView
        val url = "http://10.20.30.1"
        webView.loadUrl(url)


        b.btnKembali.setOnClickListener {
            finish()
        }
    }
}