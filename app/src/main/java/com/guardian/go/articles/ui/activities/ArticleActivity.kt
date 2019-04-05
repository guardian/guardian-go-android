package com.guardian.go.articles.ui.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guardian.go.R
import com.guardian.go.articles.ui.viewmodels.ArticleViewModel
import com.guardian.go.darkmode.DarkModeActivity

class ArticleActivity : DarkModeActivity() {

    private lateinit var splashViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        splashViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        splashViewModel.model.observe(this, Observer { model ->
            if (model != null) {

            }
        })
    }
}