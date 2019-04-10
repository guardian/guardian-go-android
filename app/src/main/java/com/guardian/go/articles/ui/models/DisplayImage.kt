package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DisplayImage @JsonCreator constructor(
    @param:JsonProperty("urlTemplate") val urlTemplate: String,
    @param:JsonProperty("height") val height: Int,
    @param:JsonProperty("width") val width: Int,
    @param:JsonProperty("orientation") val orientation: String?,
    @param:JsonProperty("caption") val caption: String?,
    @param:JsonProperty("credit") val credit: String?,
    @param:JsonProperty("altText") val altText: String?,
    @param:JsonProperty("cleanCaption") val cleanCaption: String?,
    @param:JsonProperty("cleanCredit") val cleanCredit: String?
) {
    private val _urlTemplate = ImageUrlTemplate(urlTemplate)
    val smallUrl get() = _urlTemplate.smallUrl
    val mediumUrl get() = _urlTemplate.mediumUrl
    val largeUrl get() = _urlTemplate.largeUrl
}
