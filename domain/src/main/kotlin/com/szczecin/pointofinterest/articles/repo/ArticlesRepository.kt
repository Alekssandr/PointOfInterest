package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.entities.marker.GeoSearchMain
import io.reactivex.Single

interface ArticlesRepository {
    fun fetchArticles(locationToWiki: String): Single<GeoSearchMain>
}