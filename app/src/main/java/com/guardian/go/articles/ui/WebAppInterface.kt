package com.guardian.go.articles.ui

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

/** Instantiate the interface and set the context  */
class WebAppInterface(
    private val context: Context,
    private val onCloseClick: () -> Unit
) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun updateProgress(percentage: String, articleId: String) {}

    @JavascriptInterface
    fun close() {
        onCloseClick()
    }
}