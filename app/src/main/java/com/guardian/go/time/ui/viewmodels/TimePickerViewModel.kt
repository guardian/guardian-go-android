package com.guardian.go.time.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.time.ui.data.Time
import com.guardian.go.time.ui.data.TimeRepository

class TimePickerViewModel(
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    fun load() {
        mutableModel.postValue(
            Model(
                time = timeRepository.getTime()
            )
        )
    }

    fun saveTime(hour: Int, minute: Int) {
        val time = Time(hour, minute)
        timeRepository.saveTime(time)
        mutableModel.postValue(
            Model(
                time = time
            )
        )
    }

    data class Model(
        val time: Time
    )
}