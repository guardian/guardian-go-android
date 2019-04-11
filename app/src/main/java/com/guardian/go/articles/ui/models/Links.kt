package com.guardian.go.articles.ui.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links @JsonCreator constructor(
    @param:JsonProperty("uri") val uri: String,
    @param:JsonProperty("shortUrl") val shortUrl: String,
    @param:JsonProperty("relatedUri") val relatedUri: String,
    @param:JsonProperty("webUri") val webUri: String
) : Parcelable