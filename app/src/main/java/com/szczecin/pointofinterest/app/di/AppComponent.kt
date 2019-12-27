package com.szczecin.pointofinterest.app.di

import android.app.Application
import com.szczecin.pointofinterest.app.PointOfInterestApplication
import com.szczecin.pointofinterest.di.MapsActivityBinder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class,
        MapsActivityBinder::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(pointOfInterestApplication: PointOfInterestApplication)
}