package com.guardian.go.home.ui.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import com.guardian.go.R
import com.guardian.go.darkmode.DarkModeActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : DarkModeActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(tToolbar)
        navController = findNavController(this, R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(this)
        WebView.setWebContentsDebuggingEnabled(true)
        val isNightMode = darkModeRepository.isDarkModeEnabled()
        tvNightMode.setImageResource(if (isNightMode) R.drawable.ic_night_mode else R.drawable.ic_day_mode)
        tvNightMode.setOnClickListener {
            darkModeRepository.setDarkModeEnabled(!isNightMode)
            recreate()
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.splashFragment -> hideToolbar()
            else -> showToolbar()
        }
    }

    private fun showToolbar() {
        if (tToolbar.visibility == View.GONE) {
            val constraintSet = ConstraintSet().apply {
                clone(clHome)
            }
            constraintSet.setVisibility(R.id.tToolbar, View.VISIBLE)
            constraintSet.applyTo(clHome)
        }
    }

    private fun hideToolbar() {
        if (tToolbar.visibility == View.VISIBLE) {
            val constraintSet = ConstraintSet().apply {
                clone(clHome)
            }
            constraintSet.setVisibility(R.id.tToolbar, View.GONE)
            constraintSet.applyTo(clHome)
        }
    }
}