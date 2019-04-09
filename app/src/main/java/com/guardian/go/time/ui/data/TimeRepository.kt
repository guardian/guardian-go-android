package com.guardian.go.time.ui.data

import android.content.SharedPreferences

/**
 * Repository responsible for saving and providing time.
 */
interface TimeRepository {

    fun isTime(): Boolean

    fun getTime(): Time

    fun saveTime(time: Time)

}


class SharedPreferencesTimeRepository(
    private val sharedPreferences: SharedPreferences
) : TimeRepository {

    override fun saveTime(time: Time) {
        sharedPreferences.edit().apply {
            putInt(TIME_KEY_HOUR, time.hour)
            putInt(TIME_KEY_MINUTE, time.minute)
        }.apply()
    }

    override fun isTime(): Boolean {
        return sharedPreferences.contains(TIME_KEY_HOUR) && sharedPreferences.contains(TIME_KEY_MINUTE)
    }

    override fun getTime(): Time {
        return Time(sharedPreferences.getInt(TIME_KEY_HOUR, 0), sharedPreferences.getInt(TIME_KEY_MINUTE, 0))
    }

    companion object {
        const val TIME_KEY_HOUR = "TIME_KEY_HOUR"
        const val TIME_KEY_MINUTE = "TIME_KEY_MINUTE"
    }
}