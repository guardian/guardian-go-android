package com.guardian.go.articles.ui.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class GroupReference @JsonCreator constructor(
    @param:JsonProperty("id") val id: String,
    @param:JsonProperty("title") val title: String,
    //@JsonProperty("style") style: Style? = null,
    @param:JsonProperty("uri") val uri: String?
    //@JsonProperty("userVisibility") val userVisibility: UserVisibility? = UserVisibility.ALL,
    //@param:JsonProperty("customUri") val customUri: String?,
    //@JsonProperty("personalizable") var personalizable: Boolean = true,
    //@JsonProperty("displayViewMore") val displayViewMore: Boolean = false
)