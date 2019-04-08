package com.guardian.go.settings.ui.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.guardian.go.R
import com.guardian.go.settings.ui.viewmodel.SettingsViewModel
import com.guardian.go.time.ui.data.SharedPreferencesTimeRepository

class SettingsFragment : PreferenceFragmentCompat(), TimePickerDialog.OnTimeSetListener {

    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var timePickerDialog: TimePickerDialog

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        timePickerDialog = TimePickerDialog(requireContext(), this, 0, 0, true)
        settingsViewModel = SettingsViewModel(
            SharedPreferencesTimeRepository(
                PreferenceManager.getDefaultSharedPreferences(
                    requireContext()
                )
            )
        )
        settingsViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                findPreference("pref_schedule_time").summary =
                    String.format("%02d:%02d", model.time.hour, model.time.minute)
                timePickerDialog = TimePickerDialog(requireContext(), this, model.time.hour, model.time.minute, true)
            }
        })
        settingsViewModel.load()
        (findPreference("pref_dark_mode") as SwitchPreferenceCompat).setOnPreferenceChangeListener { _, newValue ->
            if (newValue is Boolean) {
                requireActivity().recreate()
            }
            true
        }
        findPreference("pref_schedule_time").setOnPreferenceClickListener {
            timePickerDialog.show()
            true
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        settingsViewModel.saveTime(hourOfDay, minute)
    }
}
