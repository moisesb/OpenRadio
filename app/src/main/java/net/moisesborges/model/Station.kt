package net.moisesborges.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Station(
    val id: Int,
    val name: String,
    val countryCode: String,
    val image: Image,
    val thumbnail: Image,
    val genres: List<Genre>,
    val stream: Stream?
) : Parcelable {

    companion object {
        @JvmField val EMPTY_STATION = Station(
            0,
            "",
            "",
            Image(""),
            Image(""),
            emptyList(),
            Stream("", 0, "")
        )
    }
}