package com.szczecin.pointofinterest.entities.images

import com.google.gson.annotations.SerializedName

data class ImagesMain(

    @SerializedName("continue") val continueData: Continue,
    @SerializedName("query") val query: Query
)