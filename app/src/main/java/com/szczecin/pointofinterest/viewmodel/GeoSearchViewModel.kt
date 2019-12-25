package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.szczecin.pointofinterest.articles.usecase.GetNearbyArticlesUseCase
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import com.szczecin.pointofinterest.entities.marker.Article
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

interface MapsPoint {
    fun addMapsPoint(point: List<Article>)
}

class GeoSearchViewModel @Inject constructor(
    private val nearbyArticlesUseCase: GetNearbyArticlesUseCase,
    private val schedulers: RxSchedulers
) : ViewModel(), LifecycleObserver {

    private val disposables = CompositeDisposable()
    private lateinit var mapsPoint: MapsPoint

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loadGeoPages()
    }

    private fun loadGeoPages() {
        disposables += nearbyArticlesUseCase
            .execute()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("test", it.query.geoSearch[0].title)
                mapsPoint.addMapsPoint(it.query.geoSearch)
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    fun setMapsPoint(mapsPoint: MapsPoint) {
        this.mapsPoint = mapsPoint
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}