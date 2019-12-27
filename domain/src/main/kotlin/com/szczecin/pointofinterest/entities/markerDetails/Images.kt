package com.szczecin.pointofinterest.entities.markerDetails

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("ns") val ns: Int,
    @SerializedName("title") val title: String
)