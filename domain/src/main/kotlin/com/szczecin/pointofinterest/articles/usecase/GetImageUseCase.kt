package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.articles.repo.ImageRepository
import com.szczecin.pointofinterest.entities.images.ImagesMain
import io.reactivex.Single
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val articlesRepository: ImageRepository
) {
    fun execute(title: String): Single<ImagesMain> =
        articlesRepository.fetchImage(title)
}