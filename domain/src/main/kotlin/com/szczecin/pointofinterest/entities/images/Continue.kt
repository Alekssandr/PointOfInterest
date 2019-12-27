package com.szczecin.pointofinterest.entities.images

import com.google.gson.annotations.SerializedName

data class Continue(

    @SerializedName("iistart") val iistart: String,
    @SerializedName("continue") val continueData: String
)