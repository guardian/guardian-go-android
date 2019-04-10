package com.guardian.go.articles.ui.models

import android.graphics.Color
import com.fasterxml.jackson.annotation.JsonValue

import java.io.Serializable

data class ApiColour(val colour: String?) : Serializable {
    // In a ideal world we don't need this try/catch but it's here just in case an erroneous colour
    // comes from MAPI. It's better handle this in client side and return a default color, probable
    // alternative is failing to parse the whole group and display an empty container.
    val parsed: Int = try {
        if (colour.isNullOrEmpty()) Color.TRANSPARENT else Color.parseColor(colour)
    } catch (e: Exception) {
        Color.BLACK
    }

    @JsonValue
    fun toValue(): String = colour.orEmpty()
}
