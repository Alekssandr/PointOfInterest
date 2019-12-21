package com.szczecin.pointofinterest.app.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

//    @Singleton
//    @Provides
//    fun provideDatabase(context: Context): KoboldRoomDatabase = KoboldDatabase(DBConfigurationProvider(context)).build()
//
//    @Singleton
//    @Provides
//    fun provideSchedulers(): RxSchedulers = RxSchedulers()
//
//    @Singleton
//    @Provides
//    fun provideNetworkProvider(context: Context, accountRemovedChannel: AccountRemovedChannel) =
//        NetworkProvider(context, accountRemovedChannel)
//
//    @Singleton
//    @Provides
//    @CommonApiRetrofit
//    fun provideCommonApiRetrofit(
//        networkProvider: NetworkProvider
//    ): Retrofit = networkProvider.provideCommonApiRetrofit()
//
//    @Singleton
//    @Provides
//    @UserApiRetrofit
//    fun provideUserApiRetrofit(
//        networkProvider: NetworkProvider,
//        userIDStorage: UserIDStorage
//    ): Retrofit = networkProvider.provideUserApiRetrofit(userIDStorage)
//
//    @Singleton
//    @Provides
//    @UserAuthorizationApiRetrofit
//    fun provideUserAuthorizationApiRetrofit(
//        networkProvider: NetworkProvider
//    ): Retrofit = networkProvider.provideUserAuthorizationApiRetrofit()
//
//    @Singleton
//    @Provides
//    @ProductsApiRetrofit
//    fun provideProductsApiRetrofit(
//        networkProvider: NetworkProvider,
//        userIDStorage: UserIDStorage
//    ): Retrofit = networkProvider.provideProductsApiRetrofit(userIDStorage)
//
//    @Singleton
//    @Provides
//    @BeehiveAuthApiRetrofit
//    fun provideBeehiveAuthApiRetrofit(
//        robotUserStorage: RobotUserStorage
//    ): Retrofit = BeehiveNetworkProvider.provideBeehiveAuthApiRetrofit(robotUserStorage)
//
//    @Singleton
//    @Provides
//    @BeehiveApiRetrofit
//    fun provideBeehiveApiRetrofit(): Retrofit =
//        BeehiveNetworkProvider.provideBeehiveApiRetrofit()
//
//    @Singleton
//    @Provides
//    fun provideToaster(context: Context): Toaster = Toaster(context)
//
//    @Singleton
//    @Provides
//    fun provideHockeyCrashReporter(context: Context): HockeyCrashReporter = HockeyCrashReporter(context)
//
//    @Singleton
//    @Provides
//    fun provideUserIDDao(koboldRoomDatabase: KoboldRoomDatabase): UserIDDao = koboldRoomDatabase.userIDDao()
//
//    @Singleton
//    @Provides
//    fun provideUserIDStorage(userIDDao: UserIDDao, base64Converter: Base64Converter): UserIDStorage =
//        UserIDStorage(userIDDao, base64Converter)
//
//    @Singleton
//    @Provides
//    fun provideAuth0(context: Context): Auth0 = Auth0(
//        context.getString(R.string.com_auth0_client_id),
//        context.getString(R.string.com_auth0_domain)
//    )
//
//    @Singleton
//    @Provides
//    fun provideAccountRemovedObserver(): AccountRemovedChannel =
//        AccountRemovedChannel()
//
//    @Singleton
//    @Provides
//    fun provideRobotUserDao(koboldRoomDatabase: KoboldRoomDatabase): RobotUserDao =
//        koboldRoomDatabase.robotUserDao()
//
//    @Singleton
//    @Provides
//    fun provideRobotUserStorage(robotUserDao: RobotUserDao): RobotUserStorage =
//        RobotUserStorage(robotUserDao)
}