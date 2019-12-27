package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.articles.model.ArticleImages
import io.reactivex.Single

interface ImageRepository {
    fun fetchImage(title: String): Single<ArticleImages>
}