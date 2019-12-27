package com.szczecin.pointofinterest.articles.repo

import com.szczecin.pointofinterest.articles.model.MarkerDetails
import com.szczecin.pointofinterest.entities.markerDetails.MarkerDetailsMain
import io.reactivex.Single

interface MarkerDetailsRepository {
    fun fetchMarkerDetails(id: String): Single<MarkerDetails>
}