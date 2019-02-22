package net.moisesborges.base

import net.moisesborges.model.Genre
import net.moisesborges.model.Image
import net.moisesborges.model.Station
import net.moisesborges.model.Stream

fun createTestStation() = Station(
    1,
    "test station",
    "AA",
    Image("imageurl.com"),
    Image("thumbnailurl.com"),
    listOf(Genre("1", "aGenre")),
    Stream("streamurl.com", 128, "audio/mp3")
)