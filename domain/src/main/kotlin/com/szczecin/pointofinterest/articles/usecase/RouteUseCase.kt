package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.route.RouteRepository
import com.szczecin.pointofinterest.route.model.Route
import io.reactivex.Single
import javax.inject.Inject

class RouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    fun execute(startLocation: String, endLocation: String): Single<Route> =
        routeRepository.fetchDirections(startLocation, endLocation)
}