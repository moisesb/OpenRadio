package net.moisesborges.api.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.moisesborges.api.DirbleApi
import net.moisesborges.api.dirbleUrl
import net.moisesborges.api.interceptor.DirbleApiInterceptor
import net.moisesborges.api.jsonadapters.DirbleStationJsonDeserializer
import net.moisesborges.model.Station
import okhttp3.OkHttpClient
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val DIRBLE_KEY = "DIRBLE_KEY"

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

    single<Retrofit>(DIRBLE_KEY) {
        Retrofit.Builder()
            .baseUrl(dirbleUrl)
            .client(get())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get(DIRBLE_KEY)))
            .build()
    }

    single<DirbleApi> { get<Retrofit>(DIRBLE_KEY).create(DirbleApi::class.java) }
}