/*
 * MIT License
 *
 * Copyright (c) 2019 Moisés Borges dos Anjos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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