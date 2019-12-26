package com.szczecin.pointofinterest.route

import com.szczecin.pointofinterest.route.model.Route
import io.reactivex.Single

interface RouteRepository {
    fun fetchDirections(startLocation: String, endLocation: String): Single<Route>
}