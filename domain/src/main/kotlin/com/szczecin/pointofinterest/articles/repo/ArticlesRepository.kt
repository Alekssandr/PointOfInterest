package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.articles.model.GeoSearchArticle
import io.reactivex.Single

interface ArticlesRepository {
    fun fetchArticles(locationToWiki: String): Single<GeoSearchArticle>
}