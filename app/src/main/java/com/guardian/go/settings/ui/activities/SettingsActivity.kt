package com.guardian.go.settings.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.guardian.go.R
import com.guardian.go.settings.ui.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flSettings, SettingsFragment())
            .commit()
    }
}