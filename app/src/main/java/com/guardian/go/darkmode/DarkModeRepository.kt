package com.guardian.go.darkmode

import android.content.SharedPreferences

interface DarkModeRepository {
    fun isDarkModeEnabled(): Boolean
}

class SharedPreferencesDarkModeRepository(private val sharedPreferences: SharedPreferences) : DarkModeRepository {
    override fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean("pref_dark_mode", false)
    }
}