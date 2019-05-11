package net.moisesborges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stream(
    val streamUrl: String,
    val bitrate: Int?,
    val contentType: String
) : Parcelable