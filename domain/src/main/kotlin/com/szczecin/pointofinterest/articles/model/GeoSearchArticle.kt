package com.szczecin.pointofinterest.articles.model

data class GeoSearchArticle(
    val geoSearchPages: List<GeoSearchPages>
)

data class GeoSearchPages(
    var pageId: String = "",
    var title: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var dist: Double = 0.0
)