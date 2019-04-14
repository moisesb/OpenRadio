package net.moisesborges.api.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.moisesborges.api.DirbleApi
import net.moisesborges.api.KeyCdnApi
import net.moisesborges.api.dirbleUrl
import net.moisesborges.api.interceptor.DirbleApiInterceptor
import net.moisesborges.api.jsonadapters.DirbleStationJsonDeserializer
import net.moisesborges.api.jsonadapters.LocationJsonDeserializer
import net.moisesborges.api.keyCdnUrl
import net.moisesborges.features.location.Location
import net.moisesborges.model.Station
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val DIRBLE_KEY = "DIRBLE_KEY"
private const val KEY_CND_KEY = "KEY_CDN_KEY"

val apiModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(DirbleApiInterceptor(get()))
            .build()
    }

    single<Gson>(DIRBLE_KEY) {
        GsonBuilder()
            .registerTypeAdapter(Station::class.java, DirbleStationJsonDeserializer())
            .create()
    }

    single<Gson>(KEY_CND_KEY) {
        GsonBuilder()
            .registerTypeAdapter(Location::class.java, LocationJsonDeserializer())
            .create()
    }

    single<Retrofit>(DIRBLE_KEY) {
        Retrofit.Builder()
            .baseUrl(dirbleUrl)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get(DIRBLE_KEY)))
            .build()
    }

    single<Retrofit>(KEY_CND_KEY) {
        Retrofit.Builder()
            .baseUrl(keyCdnUrl)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get(KEY_CND_KEY)))
            .build()
    }

    single<DirbleApi> { get<Retrofit>(DIRBLE_KEY).create(DirbleApi::class.java) }

    single<KeyCdnApi> {  get<Retrofit>(KEY_CND_KEY).create(KeyCdnApi::class.java) }
}