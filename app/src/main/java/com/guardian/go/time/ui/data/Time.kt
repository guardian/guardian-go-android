package com.guardian.go.time.ui.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Time(
    val minutes: Int
) : Parcelable