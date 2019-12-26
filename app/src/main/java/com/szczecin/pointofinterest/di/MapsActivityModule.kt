package com.szczecin.pointofinterest.di

import android.content.Context
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.app.di.scopes.PerActivity
import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import com.szczecin.pointofinterest.articles.repo.ImageRepository
import com.szczecin.pointofinterest.articles.repo.MarkerDetailsRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.articles.repo.ArticlesDataRepository
import com.szczecin.pointofinterest.data.articles.repo.ImageDataRepository
import com.szczecin.pointofinterest.data.articles.repo.MarkerDetailsDataRepository
import com.szczecin.pointofinterest.data.route.api.GoogleDirectionsApi
import com.szczecin.pointofinterest.data.route.repo.RouteDataRepository
import com.szczecin.pointofinterest.route.RouteRepository
import dagger.Module
import dagger.Provides

@Module
class MapsActivityModule {

    @Provides
    @PerActivity
    fun provideArticlesRepository(wikiApi: WikiApi): ArticlesRepository =
        ArticlesDataRepository(wikiApi)

    @Provides
    @PerActivity
    fun provideMarkerDetailsRepository(wikiApi: WikiApi): MarkerDetailsRepository =
        MarkerDetailsDataRepository(wikiApi)

    @Provides
    @PerActivity
    fun provideImageRepository(wikiApi: WikiApi): ImageRepository =
        ImageDataRepository(wikiApi)

    @Provides
    @PerActivity
    fun provideRouteRepository(googleDirectionsApi: GoogleDirectionsApi, context: Context): RouteRepository =
        RouteDataRepository(googleDirectionsApi, context.getString(R.string.google_maps_key))
}