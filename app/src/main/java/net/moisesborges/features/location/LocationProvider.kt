package net.moisesborges.features.location

import io.reactivex.Single
import net.moisesborges.api.KeyCdnApi

class LocationProvider(private val api: KeyCdnApi) {

    private lateinit var locationCache: Single<Location>

    fun init() {
        locationCache = api.getGeolocation()
            .onErrorReturn {
                Location("", "") }
            .cache()
    }

    fun deviceLocation(): Single<Location> {
        return locationCache
    }
}