package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName

data class StartLocation(
    @SerializedName("lng")
    val lng: Double = 0.0,
    @SerializedName("lat")
    val lat: Double = 0.0
)