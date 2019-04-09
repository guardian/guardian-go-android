package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Front @JsonCreator constructor(
    @param:JsonProperty("id") val id: String,
    //@param:JsonProperty("layout") val groups: List<GroupReference>,
    //@param:JsonProperty("adverts") val adverts: Array<Advert>,
    @param:JsonProperty("title") val title: String
    //@param:JsonProperty("commercial") val commercial: Commercial,
    //@param:JsonProperty("tracking") val tracking: Tracking,
    //@param:JsonProperty("personalization") val personalisation: Personalisation?,
    //@param:JsonProperty("webUri") val webUri: String?
)