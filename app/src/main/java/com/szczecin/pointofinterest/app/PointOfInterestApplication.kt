package com.szczecin.pointofinterest.app

import android.app.Activity
import android.app.Application
import com.szczecin.pointofinterest.app.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class PointOfInterestApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        daggerInit()
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    private fun daggerInit() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}