package com.guardian.go.articles.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.articles.data.CardsRepository
import com.guardian.go.articles.ui.models.Card
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ArticleListViewModel(
    private val cardsRepository: CardsRepository
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
            compositeDisposable.add(cardsRepository
                .getCards()
                .subscribe { content ->
                    mutableModel.postValue(
                        Model(
                            loading = false,
                            cards = content
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
        val cards: List<Card> = listOf()
    )
}