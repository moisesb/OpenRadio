package net.moisesborges.base

import net.moisesborges.model.Genre
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station
import net.moisesborges.model.StreamUrl

fun createTestStation() = Station(
    1,
    "test station",
    "AA",
    ImageUrl("imageurl.com"),
    ImageUrl("thumbnailurl.com"),
    listOf(Genre("1", "aGenre")),
    StreamUrl("streamurl.com")
)