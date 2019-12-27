package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.articles.model.ArticleImages
import com.szczecin.pointofinterest.articles.repo.ImageRepository
import io.reactivex.Single
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val articlesRepository: ImageRepository
) {
    fun execute(title: String): Single<ArticleImages> =
        articlesRepository.fetchImage(title)
}