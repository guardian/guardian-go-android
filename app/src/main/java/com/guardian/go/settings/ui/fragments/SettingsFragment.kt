package com.guardian.go.settings.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.guardian.go.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
