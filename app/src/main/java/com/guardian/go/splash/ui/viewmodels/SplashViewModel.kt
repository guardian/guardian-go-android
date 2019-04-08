package com.guardian.go.splash.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.time.ui.data.TimeRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(
    private val timeRepository: TimeRepository
) : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * Load any data required for loading.
     */
    fun load() {
        mutableModel.postValue(
            Model(
                isLoading = true
            )
        )
        compositeDisposable.add(Single.fromCallable(timeRepository::isTime)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .delay(1, TimeUnit.SECONDS)
            .subscribe { isTime ->
                mutableModel.postValue(Model(isLoading = false, isTime = isTime))
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class Model(
        val isLoading: Boolean,
        val isTime: Boolean = false
    )
}