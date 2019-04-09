package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class Item @JsonCreator
constructor(
    @JsonProperty("type") contentType: ContentType,
    @param:JsonProperty("links") val links: Links,
    @param:JsonProperty("title") val title: String,
    @param:JsonProperty("webPublicationDate") val webPublicationDate: Date
    //@JsonProperty("style") style: Style? = null
)