package com.szczecin.pointofinterest.data.articles.api

import com.szczecin.pointofinterest.entities.GeoSearchMain
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApi {
    @GET("action=query")
    fun fetchArticles(
        @Query("list") list: String,
        @Query("gsradius") gsradius: String,
        @Query("gscoord") gscoord: String,
        @Query("gslimit") gslimit: String,
        @Query("format") format: String
    ): Single<List<GeoSearchMain>>
}