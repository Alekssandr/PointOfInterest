package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName

data class Duration(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("value")
    val value: Int = 0
)