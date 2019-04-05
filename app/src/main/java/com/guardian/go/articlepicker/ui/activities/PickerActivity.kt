package com.guardian.go.articlepicker.ui.activities

import android.os.Bundle
import com.guardian.go.articlepicker.ui.fragments.PickerFragment
import com.guardian.go.darkmode.DarkModeActivity

class PickerActivity : DarkModeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, PickerFragment.newInstance())
            .commit()
    }
}