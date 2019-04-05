package com.guardian.go.articlepicker.data

interface PickerContentRepository {

    fun getContent(): List<Content>
}

class TestPickerContentRepository : PickerContentRepository {
    override fun getContent(): List<Content> {
        return listOf(
            Content("Test article one", "2m"),
            Content("Test article two", "1m"),
            Content("Test article three", "10m"),
            Content("Test article four", "3m 30s"),
            Content("Test article five", "4m")
        )
    }
}