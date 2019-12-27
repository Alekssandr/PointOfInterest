package com.szczecin.pointofinterest.data.route.repo

import com.szczecin.pointofinterest.data.route.api.GoogleDirectionsApi
import com.szczecin.pointofinterest.data.mapper.toRoute
import com.szczecin.pointofinterest.route.RouteRepository
import com.szczecin.pointofinterest.route.model.Route
import io.reactivex.Single

class RouteDataRepository(
    private val api: GoogleDirectionsApi,
    private val key: String
) : RouteRepository {
    override fun fetchDirections(startLocation: String, endLocation: String): Single<Route> {
       return api.getDirections(startLocation,endLocation, key).map { it.toRoute() }
    }
}