package com.szczecin.pointofinterest.entities.images

import com.google.gson.annotations.SerializedName


data class Query(

    @SerializedName("pages") val pages: Map<String, ImageDetails>
)