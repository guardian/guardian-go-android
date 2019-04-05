package com.guardian.go.settings.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.guardian.go.R
import com.guardian.go.darkmode.DarkModeActivity
import com.guardian.go.darkmode.DarkModeRepository
import com.guardian.go.darkmode.SharedPreferencesDarkModeRepository
import com.guardian.go.settings.ui.fragments.SettingsFragment


class SettingsActivity : DarkModeActivity() {

    private val darkModeRepository: DarkModeRepository by lazy {
        SharedPreferencesDarkModeRepository(PreferenceManager.getDefaultSharedPreferences(this))
    }

    private var initialDarkModeValue: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (savedInstanceState != null) {
            initialDarkModeValue = savedInstanceState.getBoolean("KEY_OLD_NIGHT_STATE", false)
        } else {
            initialDarkModeValue = darkModeRepository.isDarkModeEnabled()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flSettings, SettingsFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (initialDarkModeValue != darkModeRepository.isDarkModeEnabled()) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val darkModeEnabled = initialDarkModeValue
        if (darkModeEnabled != null) {
            outState?.putBoolean("KEY_OLD_NIGHT_STATE", darkModeEnabled)
        }
    }
}