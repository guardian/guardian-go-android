package com.guardian.go.settings.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.guardian.go.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        (findPreference("pref_dark_mode") as SwitchPreferenceCompat).setOnPreferenceChangeListener { preference, newValue ->
            if (newValue is Boolean) {
                requireActivity().recreate()
            }
            true
        }
    }
}
