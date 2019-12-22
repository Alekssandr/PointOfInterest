package com.szczecin.pointofinterest.entities

import com.google.gson.annotations.SerializedName

data class Article (
	@SerializedName("pageid") val pageId : Int,
	@SerializedName("ns") val ns : Int,
	@SerializedName("title") val title : String,
	@SerializedName("lat") val lat : Double,
	@SerializedName("lon") val lon : Double,
	@SerializedName("dist") val dist : Double,
	@SerializedName("primary") val primary : String
)