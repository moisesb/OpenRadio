package net.moisesborges.api

import io.reactivex.Single
import net.moisesborges.features.location.Location
import retrofit2.http.GET

const val keyCdnUrl = "https://tools.keycdn.com/"

interface KeyCdnApi {

    @GET("geo.json")
    fun getGeolocation() : Single<Location>
}