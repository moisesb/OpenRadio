package net.moisesborges.api

import io.reactivex.Single
import net.moisesborges.model.Station
import retrofit2.http.GET
import retrofit2.http.Query

const val openRadioUrl = "http://openradio.moisesborges.dev/v1/"

interface OpenRadioApi {

    @GET("stations")
    fun getHomeContent(): Single<List<Station>>

    @GET("search")
    fun searchStations(@Query("name") name: String): Single<List<Station>>
}