package com.guardian.go.articlepicker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guardian.go.articlepicker.data.Content
import com.guardian.go.articlepicker.data.PickerContentRepository

class PickerViewModel(
    private val pickerContentRepository: PickerContentRepository
) : ViewModel() {

    private val mutableModel: MutableLiveData<Model> = MutableLiveData()

    val model: LiveData<Model> = mutableModel

    fun init() {
        val content = pickerContentRepository.getContent()
        mutableModel.postValue(
            Model(
                articles = content
            )
        )
    }

    data class Model(
        val articles: List<Content>
    )
}