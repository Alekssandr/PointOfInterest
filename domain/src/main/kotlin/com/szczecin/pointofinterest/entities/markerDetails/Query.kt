package com.szczecin.pointofinterest.entities.markerDetails

import com.google.gson.annotations.SerializedName


data class Query(
    @SerializedName("pages") val pages: Map<String, MarkerDetails>
)