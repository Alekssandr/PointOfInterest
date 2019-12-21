package com.szczecin.pointofinterest.data.articles.repo

import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.entities.GeoSearchMain
import io.reactivex.Single

class ArticlesDataRepository(private val api: WikiApi) : ArticlesRepository {
    companion object {
        const val LIST = "list"
        const val GSRADIUS = "10000"
        const val GSCOORD = "60.1831906%7C24.9285439"
        const val GSLIMIT = "50"
        const val FORMAT = "json"
    }
    override fun fetchArticles(): Single<List<GeoSearchMain>> {
        return api.fetchArticles(LIST,GSRADIUS,GSCOORD, GSLIMIT, FORMAT)
    }
}