package com.szczecin.pointofinterest.data.articles.api

import com.szczecin.pointofinterest.entities.GeoSearchMain
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("api.php?action=query")
    fun fetchArticles(
        @Query("list") list: String,
        @Query("gsradius") gsradius: String,
        @Query("gscoord", encoded = true) gscoord: String,
        @Query("gslimit") gslimit: String,
        @Query("format") format: String
    ): Single<GeoSearchMain>
}