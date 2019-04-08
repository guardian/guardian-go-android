package com.guardian.go.articles.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.articles.data.Content

class ArticleViewModel : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    /**
     * Load any data required for loading.
     */
    fun loadContent(content: Content) {
        mutableModel.postValue(
            Model(
                isLoading = false,
                url = content.url
            )
        )
    }

    data class Model(
        val isLoading: Boolean,
        val url: String
    )
}