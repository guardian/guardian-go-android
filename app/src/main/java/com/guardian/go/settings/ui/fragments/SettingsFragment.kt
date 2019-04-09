package com.guardian.go.settings.ui.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.guardian.go.R
import com.guardian.go.settings.ui.viewmodel.SettingsViewModel
import com.guardian.go.time.ui.data.SharedPreferencesTimeRepository

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        settingsViewModel = SettingsViewModel(
            SharedPreferencesTimeRepository(
                PreferenceManager.getDefaultSharedPreferences(
                    requireContext()
                )
            )
        )
        settingsViewModel.model.observe(this, Observer { model ->
            if (model != null) {
            }
        })
        settingsViewModel.load()
        (findPreference("pref_dark_mode") as SwitchPreferenceCompat).setOnPreferenceChangeListener { _, newValue ->
            if (newValue is Boolean) {
                requireActivity().recreate()
            }
            true
        }

    }

}
