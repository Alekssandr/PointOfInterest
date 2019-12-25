package com.szczecin.pointofinterest.data.articles.repo

import com.szczecin.pointofinterest.articles.repo.ImageRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.entities.images.ImagesMain
import io.reactivex.Single

class ImageDataRepository(private val api: WikiApi) : ImageRepository {

    companion object {
        const val PROP = "imageinfo"
        const val IIPROP = "url|dimensions"
        const val FORMAT = "json"
        const val IIURLWIDTH = "200"
    }

    override fun fetchImage(title: String): Single<ImagesMain> {
        return api.fetchImage(title, PROP, IIPROP, FORMAT, IIURLWIDTH)
    }
}