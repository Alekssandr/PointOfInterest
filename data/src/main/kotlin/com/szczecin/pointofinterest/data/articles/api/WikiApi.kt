package com.szczecin.pointofinterest.data.articles.api

import com.szczecin.pointofinterest.entities.images.ImagesMain
import com.szczecin.pointofinterest.entities.marker.GeoSearchMain
import com.szczecin.pointofinterest.entities.markerDetails.MarkerDetailsMain
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

    @GET("api.php?action=query")
    fun fetchMarkerDetails(
        @Query("prop", encoded = true) prop: String,
        @Query("pageids") pageids: String,
        @Query("format") format: String
    ): Single<MarkerDetailsMain>

    @GET("api.php?action=query")
    fun fetchImage(
        @Query("titles") titles: String,
        @Query("prop") prop: String,
        @Query("iiprop") iiprop: String,
        @Query("format") format: String,
        @Query("iiurlwidth") iiurlwidth: String
    ): Single<ImagesMain>
}