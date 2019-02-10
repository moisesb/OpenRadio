package net.moisesborges.api.jsonadapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import net.moisesborges.model.Genre
import net.moisesborges.model.Image
import net.moisesborges.model.Station
import net.moisesborges.model.Stream
import java.lang.reflect.Type

class DirbleStationJsonDeserializer : JsonDeserializer<Station> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Station {
        val stationJson = json.asJsonObject
        val imageJson = stationJson.get("image").asJsonObject
        return Station(
            stationJson.get("id").asInt,
            stationJson.get("name").asString,
            stationJson.get("country").asString,
            deserializeImage(imageJson),
            deserializeImage(imageJson.getAsJsonObject("thumbnail")),
            deserializeGenres(stationJson.getAsJsonArray("categories")),
            deserializeStream(stationJson.getAsJsonObject("stream"))
        )
    }

    private fun deserializeImage(imageJson: JsonObject?): Image {
        return if (imageJson == null) {
            Image(null)
        } else {
            val urlJson = imageJson.get("url")
            Image(if (urlJson.isJsonNull) null else urlJson.asString)
        }
    }

    private fun deserializeGenres(genresJson: JsonArray): List<Genre> {
        return genresJson.map {
            val genreJson = it.asJsonObject
            Genre(
                genreJson.get("id").asString,
                genreJson.get("title").asString
            )
        }
    }

    private fun deserializeStream(streamJson: JsonObject?): Stream? {
        return if (streamJson != null) Stream(
            streamJson.get("stream").asString,
            streamJson.get("bitrate").asInt,
            streamJson.get("content_type").asString
        ) else null
    }
}