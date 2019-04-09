package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Card @JsonCreator
constructor(
    @param:JsonProperty("importance") val importance: Int,
    @param:JsonProperty("item") val item: ArticleItem,
    @param:JsonProperty("title") val cardTitle: String? = null,
    @param:JsonProperty("rawTitle") val rawTitle: String? = null,
    @param:JsonProperty("kicker") val kicker: Kicker? = null,
    //@JsonProperty("showQuotedHeadline") showQuotedHeadline: Boolean? = false,
    @param:JsonProperty("byline") val byline: Byline? = null,
    //@param:JsonProperty("sublinks") val sublinks: List<Card>? = null,
    //@param:JsonProperty("images") val images: List<CardImage>? = null,
    @param:JsonProperty("trailText") val trailText: String? = null,
    @param:JsonProperty("mainImage") val mainImage: DisplayImage? = null,
    @param:JsonProperty("cutoutImage") val cutoutImage: DisplayImage? = null
)