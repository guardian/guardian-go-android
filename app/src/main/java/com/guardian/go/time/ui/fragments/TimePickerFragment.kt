package com.guardian.go.time.ui.fragments

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
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
        val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {


            override fun onDown(e: MotionEvent?) = true

            override fun onScroll(
                downEvent: MotionEvent,
                moveEvent: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {

                val radius = 300
                val arc =
                    Math.sqrt(
                        Math.pow(
                            (downEvent.x - moveEvent.x).toDouble(),
                            2.0
                        )
                    )

                val movingLeft = moveEvent.x < downEvent.x

                val degrees =
                    (Math.abs(arc) / radius) * if (movingLeft) -1 else 1


                vCircular.rotation = vCircular.rotation + degrees.toFloat()

                val time = Math.abs((vCircular.rotation % 360) / 360) * 99
                tvCommuteDuration.text = time.toString().replace(".", "")

                return true
            }
        })
        vCircular.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
        }

        bSetTime.setOnClickListener {
            timePickerViewModel.saveTime(tvCommuteDuration.text.toString().toInt())
            vCircular
                .animate()
                .setInterpolator(AccelerateDecelerateInterpolator())
                .rotationBy(-90f)
                .setDuration(500)
                .start()
            Navigation.findNavController(requireView())
                .navigate(TimePickerFragmentDirections.actionTimePickerFragmentToArticleListFragment())
        }
        setHasOptionsMenu(true)
    }

}