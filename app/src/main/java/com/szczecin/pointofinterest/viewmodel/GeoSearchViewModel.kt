package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.szczecin.pointofinterest.articles.model.GeoSearchPages
import com.szczecin.pointofinterest.articles.usecase.GetNearbyArticlesUseCase
import com.szczecin.pointofinterest.articles.usecase.RouteUseCase
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import com.szczecin.pointofinterest.entities.marker.Article
import com.szczecin.pointofinterest.route.model.Route
import com.szczecin.pointofinterest.route.model.StepsRoute
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

interface MapsPoint {
    fun addMapsPoint(point: List<GeoSearchPages>)
    fun getRoute(route: Route)
}

class GeoSearchViewModel @Inject constructor(
    private val nearbyArticlesUseCase: GetNearbyArticlesUseCase,
    private val routeUseCase: RouteUseCase,
    private val schedulers: RxSchedulers
) : ViewModel(), LifecycleObserver {

    val locationCoordinates = MutableLiveData<String>()
    val locationCoordinateForDirections = MutableLiveData<String>()
    val routeSuggestion = MutableLiveData<List<StepsRoute>>()
    val routeSuggestionVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility = MutableLiveData<Int>().apply {
        value = View.GONE
    }

    private val disposables = CompositeDisposable()
    private lateinit var mapsPoint: MapsPoint

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        locationCoordinates.observeForever {
            it?.let { locationCoordinates ->
                progressBarVisibility.value = View.VISIBLE
                loadGeoPages(locationCoordinates)
                locationCoordinateForDirections.value = locationCoordinates.replace("|", ",")
                routeSuggestionVisibility.value = false
            }
        }
    }

    private fun loadGeoPages(locationToWiki: String) {
        disposables += nearbyArticlesUseCase
            .execute(locationToWiki)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                mapsPoint.addMapsPoint(it.geoSearchPages)
                progressBarVisibility.value = View.GONE
            }, onError = {
                Log.e("Error", it.message ?: "")
            })
    }

    fun loadDirections(startLocation: String, endLocation: String) {
        progressBarVisibility.value = View.VISIBLE
        disposables += routeUseCase
            .execute(startLocation, endLocation)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                mapsPoint.getRoute(it)

                routeSuggestionVisibility.value = true

                routeSuggestion.value = it.steps
                progressBarVisibility.value = View.GONE
            }, onError = {
                Log.e("Error", it.message ?: "")
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