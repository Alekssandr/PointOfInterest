package com.szczecin.pointofinterest.entities.markerDetails

import com.google.gson.annotations.SerializedName


data class MarkerDetailsMain(

    @SerializedName("batchcomplete") val batchcomplete: String,
    @SerializedName("query") val query: Query
)