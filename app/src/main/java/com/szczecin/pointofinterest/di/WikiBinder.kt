package com.szczecin.pointofinterest.di

import com.szczecin.pointofinterest.app.di.scopes.PerActivity
import com.szczecin.pointofinterest.view.MapsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WikiBinder {

    @ContributesAndroidInjector(modules = [MapsActivityModule::class])
    @PerActivity
    abstract fun bindSplashActivity(): MapsActivity
}