package com.guardian.go.articles.ui.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Palette constructor(
    val backgroundColour: ApiColour,
    val pillarColour: ApiColour,
    val mainColour: ApiColour,
    val headlineColour: ApiColour,
    val commentCountColour: ApiColour,
    val metaColour: ApiColour,
    val elementBackground: ApiColour,
    val shadowColour: ApiColour,
    val immersiveKicker: ApiColour,
    val secondaryColour: ApiColour
) : Parcelable {
    @JsonCreator constructor(
        @JsonProperty("background") background: String?,
        @JsonProperty("pillar") pillar: String?,
        @JsonProperty("main") main: String?,
        @JsonProperty("headline") headline: String?,
        @JsonProperty("commentCount") commentCount: String?,
        @JsonProperty("metaText") metaText: String?,
        @JsonProperty("elementBackground") elementBackground: String?,
        @JsonProperty("shadow") shadow: String?,
        @JsonProperty("immersiveKicker") immersiveKicker: String?,
        @JsonProperty("secondary") secondary: String?
    ) : this(
        backgroundColour = ApiColour(background),
        pillarColour = ApiColour(pillar),
        mainColour = ApiColour(main),
        headlineColour = ApiColour(headline),
        commentCountColour = ApiColour(commentCount),
        metaColour = ApiColour(metaText),
        elementBackground = ApiColour(elementBackground),
        shadowColour = ApiColour(shadow),
        immersiveKicker = ApiColour(immersiveKicker),
        secondaryColour = ApiColour(secondary)
    )

    val linesColour: ApiColour get() = mainColour
    val kickerColour: ApiColour get() = mainColour
    val bylineColour: ApiColour get() = mainColour
    val headlineIconColour: ApiColour get() = mainColour
    val trailTextColour: ApiColour get() = metaColour
    val buttonBackgroundColour: ApiColour get() = elementBackground
    val cutoutBackgroundColour: ApiColour get() = elementBackground
    val reviewStarsColour: ApiColour get() = ApiColour("#121212")
}
