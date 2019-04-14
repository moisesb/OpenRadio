package net.moisesborges.api.jsonadapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import net.moisesborges.features.location.Location
import java.lang.reflect.Type

class LocationJsonDeserializer : JsonDeserializer<Location> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Location {
        val responseJson = json.asJsonObject
        val data = responseJson.get("data")
        if (data.isJsonNull) {
            return Location("", "")
        }
        val geo = data.asJsonObject.get("geo").asJsonObject
        val countryCode = geo.get("country_code").asString
        val localityCode = geo.get("region_code").asString
        return Location(countryCode, localityCode)
    }
}