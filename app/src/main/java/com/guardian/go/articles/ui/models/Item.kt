package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@Deprecated("Just use modified ArticleItem in this project")
private open class Item @JsonCreator
constructor(
    @param:JsonProperty("type") val contentType: String,
    @param:JsonProperty("links") val links: Links,
    @param:JsonProperty("title") val title: String,
    @param:JsonProperty("webPublicationDate") val webPublicationDate: Date
    //@JsonProperty("style") style: Style? = null
)

data class ArticleItem @JsonCreator
constructor(
    @param:JsonProperty("type") val type: String,
    @param:JsonProperty("designType") val designType: String,
    @param:JsonProperty("id") val id: String,
    @param:JsonProperty("title") val title: String,
    @param:JsonProperty("trailText") val trailText: String?,
    @param:JsonProperty("byline") val byline: String,
    @param:JsonProperty("bodyImages") val bodyImages: List<DisplayImage>,
    @param:JsonProperty("displayImages") val displayImages: List<DisplayImage>,
    @param:JsonProperty("section") val section: String,
    @param:JsonProperty("standFirst") val standFirst: String,
    @param:JsonProperty("webPublicationDate") val webPublicationDate: Date,
    @param:JsonProperty("links") val links: Links,
    //@param:JsonProperty("metadata") val metadata: Metadata,
    //@JsonProperty("style") style: Style,
    @param:JsonProperty("palette") val palette: Palette,
    //@param:JsonProperty("footballContent") val footballContent: FootballMatch? = null,
    //@param:JsonProperty("cricketContent") val cricketContent: CricketContent? = null,
    //@param:JsonProperty("video") val video: Video? = null,
    //@param:JsonProperty("starRating") val starRating: Int? = null,
    //@param:JsonProperty("audio") val audio: Audio? = null,
    //@param:JsonProperty("latestBlock") val latestBlock: Block? = null,
    @param:JsonProperty("body") val body: String? = null,
    @param:JsonProperty("headerImage") val headerImage: DisplayImage? = null,
    //@param:JsonProperty("liveContent") val liveContent: LiveContent? = null,
    //@JsonProperty("shouldHideAdverts") shouldHideAdverts: Boolean? = null,
    //@JsonProperty("shouldHideReaderRevenue") shouldHideReaderRevenue: Boolean? = null,
    //@param:JsonProperty("headerEmbed") val headerEmbed: String? = null,
    //@param:JsonProperty("headerVideo") val headerVideo: Video? = null,
    //@param:JsonProperty("branding") val branding: Branding? = null,
    //@param:JsonProperty("headerAtom") val headerAtom: String? = null,
    @param:JsonProperty("editedTrailText") val editedTrailText: String? = null,
    //@param:JsonProperty("atoms") val atoms: Array<Atom>? = null,
    @param:JsonProperty("pillar") val pillar: Pillar? = null
    //@param:JsonProperty("atomsCSS") val atomsCSS: Array<String>? = null,
    //@param:JsonProperty("atomsJS") val atomsJS: Array<String>? = null
)