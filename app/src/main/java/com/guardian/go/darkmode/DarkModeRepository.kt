package com.guardian.go.darkmode

import android.content.SharedPreferences

interface DarkModeRepository {
    fun isDarkModeEnabled(): Boolean
    fun setDarkModeEnabled(enabled: Boolean)
}

class SharedPreferencesDarkModeRepository(private val sharedPreferences: SharedPreferences) : DarkModeRepository {
    override fun setDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("pref_dark_mode", enabled).apply()
    }

    override fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean("pref_dark_mode", false)
    }
}