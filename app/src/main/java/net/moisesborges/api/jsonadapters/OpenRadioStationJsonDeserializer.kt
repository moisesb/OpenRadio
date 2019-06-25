package net.moisesborges.api.jsonadapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import net.moisesborges.model.ImageUrl
import net.moisesborges.model.Station
import net.moisesborges.model.StreamUrl
import java.lang.reflect.Type

class OpenRadioStationJsonDeserializer : JsonDeserializer<Station> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Station {
        val stationJson = json.asJsonObject
        return Station(
            stationJson.get("id").asInt,
            stationJson.get("name").asString,
            stationJson.get("countryCode").asString,
            ImageUrl(stationJson.get("thumbnailUrl").asString),
            ImageUrl(stationJson.get("thumbnailUrl").asString),
            emptyList(),
            StreamUrl(stationJson.get("streamUrl").asString)
        )
    }
}