package com.guardian.go.darkmode

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class DarkModeActivity : AppCompatActivity() {

    private val darkModeRepository: DarkModeRepository by lazy {
        SharedPreferencesDarkModeRepository(PreferenceManager.getDefaultSharedPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkModeRepository.isDarkModeEnabled()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        super.onCreate(savedInstanceState)
    }
}