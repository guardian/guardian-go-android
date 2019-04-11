package com.guardian.go.articles.ui

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface(
    private val context: Context,
    private val onCloseClick: () -> Unit
) {

    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun updateProgress(percentage: String) {}

    @JavascriptInterface
    fun close() {
        onCloseClick()
    }
}