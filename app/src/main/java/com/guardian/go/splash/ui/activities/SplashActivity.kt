package com.guardian.go.splash.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guardian.go.R
import com.guardian.go.articles.ui.activities.ArticleActivity
import com.guardian.go.settings.ui.activities.SettingsActivity
import com.guardian.go.splash.ui.viewmodels.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        splashViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                pbLoading.visibility = if (model.isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })

        bArticle.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }
        bSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}