package com.szczecin.pointofinterest.data.route.mapper

import com.szczecin.pointofinterest.route.model.Route
import com.szczecin.pointofinterest.entities.route.Directions

fun Directions.toRoute(): Route {
    val legs = routes[0].legs[0]
    return Route(
        "curentAdress",
        legs.endLocation.toString(),
        legs.startLocation.lat,
        legs.startLocation.lng,
        legs.endLocation.lat,
        legs.endLocation.lng,
        routes[0].overviewPolyline.points
    )
}

