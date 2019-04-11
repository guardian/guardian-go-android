package com.guardian.go.articles.ui.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Style(val ruleColour: ApiColour) {

    @JsonCreator
    constructor(
        @JsonProperty("ruleColour") ruleColour: String
    ) : this(
        ruleColour = ApiColour(ruleColour)
    )
}