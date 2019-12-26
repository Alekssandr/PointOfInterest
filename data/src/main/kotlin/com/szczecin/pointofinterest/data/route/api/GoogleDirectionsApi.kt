package com.szczecin.pointofinterest.data.route.api

import com.szczecin.pointofinterest.entities.route.Directions
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleDirectionsApi {

    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Single<Directions>
}