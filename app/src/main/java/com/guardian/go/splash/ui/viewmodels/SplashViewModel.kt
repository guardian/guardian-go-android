package com.guardian.go.splash.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    /**
     * Load any data required for loading.
     */
    fun load() {
        mutableModel.postValue(
            Model(
                isLoading = true
            )
        )
    }

    data class Model(
        val isLoading: Boolean
    )
}