package net.moisesborges.api

import io.reactivex.Single
import net.moisesborges.model.Station
import retrofit2.http.GET

const val dirbleUrl = "http://api.dirble.com/v2/"

interface DirbleApi {

    @GET("stations/popular")
    fun getPopularStations(): Single<List<Station>>
}