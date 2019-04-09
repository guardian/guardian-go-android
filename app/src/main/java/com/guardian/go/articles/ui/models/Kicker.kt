package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class Kicker @JsonCreator constructor(
    @param:JsonProperty("title") val title: String
)