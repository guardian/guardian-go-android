package com.guardian.go.time.ui.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.guardian.go.R
import com.guardian.go.time.ui.data.SharedPreferencesTimeRepository
import com.guardian.go.time.ui.viewmodels.TimePickerViewModel
import kotlinx.android.synthetic.main.fragment_time_picker.*

class TimePickerFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private val timePickerDialog: TimePickerDialog by lazy {
        TimePickerDialog(requireContext(), this, 0, 0, true)
    }

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
                tvCommuteTime.text = String.format("%02d:%02d", model.time.hour, model.time.minute)
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
        bSetDuration.setOnClickListener {
            timePickerDialog.show()
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_time_picker, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_time -> {
                Navigation.findNavController(requireView())
                    .navigate(TimePickerFragmentDirections.actionTimePickerFragmentToHomeFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timePickerViewModel.saveTime(hourOfDay, minute)
    }
}