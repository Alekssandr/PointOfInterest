package com.szczecin.pointofinterest.entities.marker

import com.google.gson.annotations.SerializedName

data class GeoSearch(
    @SerializedName("geosearch") val geoSearch: List<Article>
)