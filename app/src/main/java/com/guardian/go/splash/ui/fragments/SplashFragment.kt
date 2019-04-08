package com.guardian.go.splash.ui.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.guardian.go.R
import com.guardian.go.splash.ui.viewmodels.SplashViewModel
import com.guardian.go.time.ui.data.SharedPreferencesTimeRepository
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)
        splashViewModel =
            SplashViewModel(SharedPreferencesTimeRepository(PreferenceManager.getDefaultSharedPreferences(requireContext())))
        splashViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                if (model.isLoading) {
                    pbLoading.visibility = View.VISIBLE
                } else {
                    pbLoading.visibility = View.GONE
                    if (model.isTime) {
                        navController.navigate(SplashFragmentDirections.actionSplashToHome())
                    } else {
                        navController.navigate(SplashFragmentDirections.actionSplashFragmentToTimePickerFragment())
                    }
                }
            }
        })
        splashViewModel.load()
    }
}