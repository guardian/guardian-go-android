package com.guardian.go.articles.ui.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Style(val ruleColour: ApiColour): Parcelable {

    @JsonCreator
    constructor(
        @JsonProperty("ruleColour") ruleColour: String
    ) : this(
        ruleColour = ApiColour(ruleColour)
    )
}