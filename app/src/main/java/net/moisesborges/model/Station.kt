package net.moisesborges.model

data class Station(
    val id: Int,
    val name: String,
    val countryCode: String,
    val image: Image,
    val thumbnail: Image,
    val genres: List<Genre>,
    val stream: Stream?
)