package com.guardian.go.time.ui.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.guardian.go.R
import com.guardian.go.time.ui.data.SharedPreferencesTimeRepository
import com.guardian.go.time.ui.viewmodels.TimePickerViewModel
import kotlinx.android.synthetic.main.fragment_time_picker.*

class TimePickerFragment : Fragment() {

    private lateinit var timePickerViewModel: TimePickerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timePickerViewModel = TimePickerViewModel(
            SharedPreferencesTimeRepository(
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            )
        )
        timePickerViewModel.model.observe(this, Observer { model ->
            if (model != null) {
                tvCommuteDuration.setText(model.time.minutes.toString())
            }
        })
        timePickerViewModel.load()
        tvCommuteDuration.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Empty.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Empty.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val multiplier = if (count == 1) 1 else -1
                vCircular
                    .animate()
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .rotationBy(multiplier * 25f)
                    .setDuration(500)
                    .start()
            }

        })

        bSetTime.setOnClickListener {
            timePickerViewModel.saveTime(tvCommuteDuration.text.toString().toInt())
            vCircular
                .animate()
                .setInterpolator(AccelerateDecelerateInterpolator())
                .rotationBy(-90f)
                .setDuration(500)
                .start()
            Navigation.findNavController(requireView())
                .navigate(TimePickerFragmentDirections.actionTimePickerFragmentToHomeFragment())
        }
        setHasOptionsMenu(true)
    }
}