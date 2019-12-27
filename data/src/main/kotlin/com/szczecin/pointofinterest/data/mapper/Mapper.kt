package com.szczecin.pointofinterest.data.mapper

import com.szczecin.pointofinterest.articles.model.ArticleImages
import com.szczecin.pointofinterest.articles.model.GeoSearchArticle
import com.szczecin.pointofinterest.articles.model.GeoSearchPages
import com.szczecin.pointofinterest.articles.model.MarkerDetails
import com.szczecin.pointofinterest.entities.images.ImagesMain
import com.szczecin.pointofinterest.entities.marker.GeoSearchMain
import com.szczecin.pointofinterest.entities.markerDetails.MarkerDetailsMain
import com.szczecin.pointofinterest.entities.route.Directions
import com.szczecin.pointofinterest.route.model.Route
import com.szczecin.pointofinterest.route.model.StepsRoute

fun Directions.toRoute(): Route {
    val legs = routes[0].legs[0]
    return Route(
        legs.startLocation.lat,
        legs.startLocation.lng,
        legs.endLocation.lat,
        legs.endLocation.lng,
        routes[0].overviewPolyline.points,
        mutableListOf<StepsRoute>().apply {
            routes[0].legs[0].steps?.forEach {
                StepsRoute().run {
                    duration = it.duration.text
                    distance = it.distance.text
                    travelMode = it.travelMode
                    htmlInstructions = it.htmlInstructions
                    add(this)
                }
            }
        }
    )
}

fun GeoSearchMain.toGeoSearchArticle(): GeoSearchArticle {
    return GeoSearchArticle(
        mutableListOf<GeoSearchPages>().apply {
            query.geoSearch.forEach {
                GeoSearchPages().run {
                    pageId = it.pageId
                    title = it.title
                    lat = it.lat
                    lon = it.lon
                    dist = it.dist
                    add(this)
                }
            }
        }
    )
}

fun ImagesMain.toArticleImages(): ArticleImages =
    ArticleImages(
        mutableListOf<String>().apply {
            for ((_, value) in query.pages) {
                add(value.imageInfo[0].thumburl)
            }
        }
    )

fun MarkerDetailsMain.toMarkerDetails(): MarkerDetails {
    val marker = query.pages[query.pages.keys.first()]
    return MarkerDetails(
        title = marker?.title ?: "",
        description = marker?.description ?: "",
        imageUrl = mutableListOf<String>().apply {
            marker?.images?.forEach {
                add(it.title)
            }
        }
    )
}
