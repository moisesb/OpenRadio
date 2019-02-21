package net.moisesborges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val id: String,
    val title: String
) : Parcelable