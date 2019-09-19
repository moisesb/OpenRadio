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

package net.moisesborges.api.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.moisesborges.api.openRadioUrl
import net.moisesborges.api.keyCdnUrl
import net.moisesborges.api.OpenRadioApi
import net.moisesborges.api.KeyCdnApi
import net.moisesborges.api.jsonadapters.LocationJsonDeserializer
import net.moisesborges.api.jsonadapters.OpenRadioStationJsonDeserializer
import net.moisesborges.features.location.Location
import net.moisesborges.model.Station
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val KEY_CND_KEY = "KEY_CDN_KEY"
private const val OPEN_RADIO_KEY = "OPEN_RADIO_KEY"

val apiModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .build()
    }

        single<Gson>(KEY_CND_KEY) {
        GsonBuilder()
            .registerTypeAdapter(Location::class.java, LocationJsonDeserializer())
            .create()
    }

    single<Gson>(OPEN_RADIO_KEY) {
        GsonBuilder()
            .registerTypeAdapter(Station::class.java, OpenRadioStationJsonDeserializer())
            .create()
    }

    single<Retrofit>(KEY_CND_KEY) {
        Retrofit.Builder()
            .baseUrl(keyCdnUrl)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get(KEY_CND_KEY)))
            .build()
    }

    single<Retrofit>(OPEN_RADIO_KEY) {
        Retrofit.Builder()
            .baseUrl(openRadioUrl)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get(OPEN_RADIO_KEY)))
            .build()
    }

    single<KeyCdnApi> { get<Retrofit>(KEY_CND_KEY).create(KeyCdnApi::class.java) }

    single<OpenRadioApi> { get<Retrofit>(OPEN_RADIO_KEY).create(OpenRadioApi::class.java) }
}