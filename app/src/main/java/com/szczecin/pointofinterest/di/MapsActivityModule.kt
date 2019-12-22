package com.szczecin.pointofinterest.di

import com.szczecin.pointofinterest.app.di.scopes.PerActivity
import com.szczecin.pointofinterest.articles.repo.ArticlesRepository
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.articles.repo.ArticlesDataRepository
import dagger.Module
import dagger.Provides

@Module
class MapsActivityModule {

    @Provides
    @PerActivity
    fun provideUserLoginStatusRepository(wikiApi: WikiApi): ArticlesRepository =
        ArticlesDataRepository(wikiApi)
}