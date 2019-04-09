package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Pillar @JsonCreator constructor(
    @param:JsonProperty("id") val id: String,
    @param:JsonProperty("name") val name: String
)
