package com.szczecin.pointofinterest.route.model

data class Route(
    val startLat: Double?,
    val startLng: Double?,
    val endLat: Double?,
    val endLng: Double?,
    val overviewPolyline: String = "",
    val steps: List<StepsRoute>
)

data class StepsRoute(
    var duration: String = "",
    var distance: String = "",
    var travelMode: String = "",
    var htmlInstructions: String = ""
)


