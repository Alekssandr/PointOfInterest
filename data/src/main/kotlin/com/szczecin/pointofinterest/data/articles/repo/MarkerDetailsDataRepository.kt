package com.szczecin.pointofinterest.data.articles.repo

import com.szczecin.pointofinterest.articles.model.MarkerDetails
import com.szczecin.pointofinterest.articles.repo.MarkerDetailsRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.mapper.toMarkerDetails
import io.reactivex.Single

class MarkerDetailsDataRepository(private val api: WikiApi) : MarkerDetailsRepository {

    companion object {
        const val PROP = "info%7Cdescription%7Cimages"
        const val FORMAT = "json"
    }

    override fun fetchMarkerDetails(id: String): Single<MarkerDetails> {
        return api.fetchMarkerDetails(PROP, id, FORMAT).map { it.toMarkerDetails() }
    }
}