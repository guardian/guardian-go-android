package com.guardian.go.articlepicker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.articlepicker.data.Content
import com.guardian.go.articlepicker.data.PickerContentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PickerViewModel(
    private val pickerContentRepository: PickerContentRepository
) : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadArticles() {
        if (model.value == null) {
            mutableModel.postValue(
                Model(
                    loading = true
                )
            )
            compositeDisposable.add(pickerContentRepository
                .getContent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { content ->
                    mutableModel.postValue(
                        Model(
                            loading = false,
                            articles = content
                        )
                    )
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    data class Model(
        val loading: Boolean,
        val articles: List<Content> = listOf()
    )
}