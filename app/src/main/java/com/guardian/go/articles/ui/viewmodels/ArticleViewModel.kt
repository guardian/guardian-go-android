package com.guardian.go.articles.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {

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