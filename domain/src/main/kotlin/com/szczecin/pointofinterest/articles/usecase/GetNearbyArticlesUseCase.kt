package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.articles.model.GeoSearchArticle
import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetNearbyArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) {
    fun execute(locationToWiki: String): Single<GeoSearchArticle> =
        articlesRepository.fetchArticles(locationToWiki)
}