package net.moisesborges.api

import io.reactivex.Single
import net.moisesborges.model.Station
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val openRadioUrl = "http://openradio.moisesborges.dev/v1/"

interface OpenRadioApi {

    @GET("home")
    fun getHomeContent(): Single<List<Station>>

    @POST("search")
    fun searchStations(query: String, @Query("country") country: String): Single<List<Station>>
}