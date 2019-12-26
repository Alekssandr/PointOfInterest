package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.szczecin.pointofinterest.articles.usecase.GetNearbyArticlesUseCase
import com.szczecin.pointofinterest.articles.usecase.RouteUseCase
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import com.szczecin.pointofinterest.entities.marker.Article
import com.szczecin.pointofinterest.route.model.Route
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

interface MapsPoint {
    fun addMapsPoint(point: List<Article>)
    fun getRoute(route: Route)
}

class GeoSearchViewModel @Inject constructor(
    private val nearbyArticlesUseCase: GetNearbyArticlesUseCase,
    private val routeUseCase: RouteUseCase,
    private val schedulers: RxSchedulers
) : ViewModel(), LifecycleObserver {

    private val disposables = CompositeDisposable()
    private lateinit var mapsPoint: MapsPoint
    val locationCoordinates = MutableLiveData<String>()
    val locationCoordinateForDirections = MutableLiveData<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        locationCoordinates.observeForever {
            it?.let { locationCoordinates ->
                loadGeoPages(locationCoordinates)
                locationCoordinateForDirections.value = locationCoordinates.replace("|",",")
            }
        }
    }

    private fun loadGeoPages(locationToWiki: String) {
        disposables += nearbyArticlesUseCase
            .execute(locationToWiki)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("test", it.query.geoSearch[0].title)
                mapsPoint.addMapsPoint(it.query.geoSearch)
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    fun loadDirections(startLocation: String, endLocation: String) {
        disposables += routeUseCase
            .execute(startLocation,endLocation)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                mapsPoint.getRoute(it)
                Log.d("test", it.toString())
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