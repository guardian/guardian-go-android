package com.guardian.go.splash.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.guardian.go.R
import com.guardian.go.articlepicker.ui.activities.PickerActivity
import com.guardian.go.articles.ui.activities.ArticleActivity
import com.guardian.go.darkmode.DarkModeActivity
import com.guardian.go.settings.ui.activities.SettingsActivity
import com.guardian.go.splash.ui.viewmodels.SplashViewModel
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : DarkModeActivity() {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel = SplashViewModel()
        splashViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                pbLoading.visibility = if (model.isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
        splashViewModel.load()
        bArticle.setOnClickListener {
            val intent = Intent(this, ArticleActivity::class.java)
            startActivity(intent)
        }
        bSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, REQUEST_NIGHT_MODE)
        }
        bPicker.setOnClickListener {
            val intent = Intent(this, PickerActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_NIGHT_MODE && resultCode == Activity.RESULT_OK) {
            recreate()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val REQUEST_NIGHT_MODE = 1001
    }
}