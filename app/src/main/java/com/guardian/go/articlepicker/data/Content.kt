package com.guardian.go.articlepicker.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    val title: String,
    val duration: String,
    val url: String
) : Parcelable