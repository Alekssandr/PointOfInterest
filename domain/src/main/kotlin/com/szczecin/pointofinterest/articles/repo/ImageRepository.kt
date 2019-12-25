package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.entities.images.ImagesMain
import io.reactivex.Single

interface ImageRepository {
    fun fetchImage(title: String): Single<ImagesMain>
}