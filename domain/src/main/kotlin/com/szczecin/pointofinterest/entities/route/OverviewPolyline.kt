package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName

data class OverviewPolyline(
    @SerializedName("points")
    val points: String = ""
)