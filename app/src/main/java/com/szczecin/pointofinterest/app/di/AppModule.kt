package com.szczecin.pointofinterest.app.di

import android.app.Application
import android.content.Context
import com.szczecin.pointofinterest.BuildConfig
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import com.szczecin.pointofinterest.data.articles.api.WikiApi
import com.szczecin.pointofinterest.data.network.NetworkProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun profivdeStoresApi(retrofit: Retrofit): WikiApi = retrofit.create(WikiApi::class.java)

    @Singleton
    @Provides
    fun provideSchedulers(): RxSchedulers = RxSchedulers()

    @Singleton
    @Provides
    fun provideNetworkProvider() = NetworkProvider(BuildConfig.API_URL)

    @Singleton
    @Provides
    fun provideCommonApiRetrofit(
        networkProvider: NetworkProvider
    ): Retrofit = networkProvider.provideCommonApiRetrofit()

}