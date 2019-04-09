package com.guardian.go.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.time.ui.data.Time
import com.guardian.go.time.ui.data.TimeRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SettingsViewModel(
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun load() {
        compositeDisposable.add(Single.fromCallable(timeRepository::getTime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { time ->
                mutableModel.postValue(
                    Model(
                        time = time
                    )
                )
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun saveTime(minutes: Int) {
        val time = Time(minutes)
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