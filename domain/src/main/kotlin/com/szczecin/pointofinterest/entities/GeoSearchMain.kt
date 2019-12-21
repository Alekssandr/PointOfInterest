package com.szczecin.pointofinterest.entities

import com.google.gson.annotations.SerializedName

data class GeoSearchMain (
	@SerializedName("batchcomplete") val batchComplete : String,
	@SerializedName("query") val query : GeoSearch
)