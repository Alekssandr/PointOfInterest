package com.szczecin.pointofinterest.app.di

import android.app.Application
import android.content.Context
import com.szczecin.pointofinterest.BuildConfig
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.network.NetworkProvider
import com.szczecin.pointofinterest.data.route.api.GoogleDirectionsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideWikiApi(retrofit: Retrofit): WikiApi = retrofit.create(WikiApi::class.java)

    @Singleton
    @Provides
    fun provideDirectionsApi(@Named("directions")retrofit: Retrofit): GoogleDirectionsApi = retrofit.create(GoogleDirectionsApi::class.java)

    @Singleton
    @Provides
    fun provideSchedulers(): RxSchedulers = RxSchedulers()

    @Singleton
    @Provides
    fun provideNetworkProvider() = NetworkProvider()

    @Singleton
    @Provides
    fun provideCommonApiRetrofit(
        networkProvider: NetworkProvider
    ): Retrofit = networkProvider.provideCommonApiRetrofit(BuildConfig.API_URL)

    @Singleton
    @Provides
    @Named("directions")
    fun provideCommonApiRetrofitRoute(
        networkProvider: NetworkProvider
    ): Retrofit = networkProvider.provideCommonApiRetrofit(BuildConfig.API_ROUTE_URL)
}