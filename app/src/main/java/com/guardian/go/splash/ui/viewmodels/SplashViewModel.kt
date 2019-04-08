package com.guardian.go.splash.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashViewModel : ViewModel() {

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
        compositeDisposable.add(Single.just(1).delay(1, TimeUnit.SECONDS).subscribe { value ->
            mutableModel.postValue(Model(isLoading = false))
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class Model(
        val isLoading: Boolean
    )
}