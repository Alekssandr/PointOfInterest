package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName

data class Polyline(
    @SerializedName("points")
    val points: String = ""
)