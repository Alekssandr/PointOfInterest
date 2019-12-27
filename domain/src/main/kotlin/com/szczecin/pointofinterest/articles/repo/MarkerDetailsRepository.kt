package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.articles.model.MarkerDetails
import io.reactivex.Single

interface MarkerDetailsRepository {
    fun fetchMarkerDetails(id: String): Single<MarkerDetails>
}