package com.guardian.go.home.ui.activities

import android.os.Bundle
import com.guardian.go.R
import com.guardian.go.darkmode.DarkModeActivity

class HomeActivity : DarkModeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}