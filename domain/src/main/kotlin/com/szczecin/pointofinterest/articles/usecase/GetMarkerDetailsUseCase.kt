package com.szczecin.pointofinterest.articles.usecase

import com.szczecin.pointofinterest.articles.model.MarkerDetails
import com.szczecin.pointofinterest.articles.repo.MarkerDetailsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMarkerDetailsUseCase @Inject constructor(
    private val markerDetailsRepository: MarkerDetailsRepository
) {
    fun execute(id: String): Single<MarkerDetails> =
        markerDetailsRepository.fetchMarkerDetails(id)
}