package net.moisesborges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    val id: Int,
    val name: String,
    val countryCode: String,
    val imageUrl: ImageUrl,
    val thumbnailUrl: ImageUrl,
    val genres: List<Genre>,
    val streamUrl: StreamUrl
) : Parcelable {

    companion object {
        @JvmField val EMPTY_STATION = Station(
            0,
            "",
            "",
            ImageUrl(""),
            ImageUrl(""),
            emptyList(),
            StreamUrl("")
        )
    }
}