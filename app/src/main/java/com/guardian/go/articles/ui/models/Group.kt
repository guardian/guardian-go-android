package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class Group @JsonCreator constructor(
    @param:JsonProperty("id") val id: String,
    @param:JsonProperty("title") val title: String,
    @JsonProperty("cards") cards: List<Card>,
    //@param:JsonProperty("followUp") val followUp: FollowUp? = null,
    //@JsonProperty("style") style: Style? = null,
    //@param:JsonProperty("personalization") val personalisation: Personalisation? = null,
    @JsonProperty("lastModified") lastModified: Date? = null,
    @param:JsonProperty("description") val description: String? = null
    //@JsonProperty("isThrasherCollection") isThrasher: Boolean? = false,
    //@param:JsonProperty("thrasher") val thrasher: Thrasher? = null,
    //@JsonProperty("userVisibility") val userVisibility: UserVisibility? = UserVisibility.ALL,
    //@JsonProperty("branded") branded: Boolean? = false,
    //@param:JsonProperty("commercial") val commercial: Commercial? = null,
    //@param:JsonProperty("collectionType") val collectionLayoutName: String? = null,
    //@param:JsonProperty("topic") val topic: Topics? = null
)