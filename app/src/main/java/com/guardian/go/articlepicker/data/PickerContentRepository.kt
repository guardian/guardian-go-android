package com.guardian.go.articlepicker.data

import io.reactivex.Single
import java.util.concurrent.TimeUnit

interface PickerContentRepository {

    fun getContent(): Single<List<Content>>
}

class TestPickerContentRepository : PickerContentRepository {
    override fun getContent(): Single<List<Content>> {
        return Single.just(
            listOf(
                Content("Test article one", "2m"),
                Content("Test article two", "1m"),
                Content("Test article three", "10m"),
                Content("Test article four", "3m 30s"),
                Content("Test article five", "4m")
            )
        ).delay(1, TimeUnit.SECONDS)
    }
}