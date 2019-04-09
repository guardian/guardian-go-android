package com.guardian.go.time.ui.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Time(
    val hour: Int,
    val minute: Int
) : Parcelable