package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Byline @JsonCreator constructor(
    @param:JsonProperty("title") val title: String
)