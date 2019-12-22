package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.entities.GeoSearchMain
import io.reactivex.Single

interface ArticlesRepository {
    fun fetchArticles(): Single<GeoSearchMain>
}