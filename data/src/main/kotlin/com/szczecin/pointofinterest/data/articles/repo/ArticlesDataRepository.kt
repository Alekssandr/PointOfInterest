package com.szczecin.pointofinterest.data.articles.repo

import com.szczecin.pointofinterest.articles.model.GeoSearchArticle
import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.mapper.toGeoSearchArticle
import com.szczecin.pointofinterest.entities.marker.GeoSearchMain
import io.reactivex.Single

class ArticlesDataRepository(private val api: WikiApi) : ArticlesRepository {
    companion object {
        const val LIST = "geosearch"
        const val GSRADIUS = "10000"
        const val GSLIMIT = "50"
        const val FORMAT = "json"
    }

    override fun fetchArticles(locationToWiki: String): Single<GeoSearchArticle> {
        return api.fetchArticles(LIST, GSRADIUS, locationToWiki, GSLIMIT, FORMAT).map { it.toGeoSearchArticle() }
    }
}