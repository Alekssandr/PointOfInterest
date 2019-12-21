package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import com.szczecin.pointofinterest.entities.GeoSearchMain
import io.reactivex.Single
import javax.inject.Inject

class GetNearbyArticlesUseCase @Inject constructor(
    private val articlesRepository: ArticlesRepository
) {
    fun execute(): Single<List<GeoSearchMain>> =
        articlesRepository.fetchArticles()
}