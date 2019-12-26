package com.szczecin.pointofinterest.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.szczecin.pointofinterest.BuildConfig
import com.szczecin.pointofinterest.articles.usecase.GetImageUseCase
import com.szczecin.pointofinterest.articles.usecase.GetMarkerDetailsUseCase
import com.szczecin.pointofinterest.common.rx.RxSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MarkerDetailsViewModel @Inject constructor(
    private val markerDetailsUseCase: GetMarkerDetailsUseCase,
    private val imageUseCase: GetImageUseCase,
    private val schedulers: RxSchedulers
) : ViewModel(), LifecycleObserver {

    private val disposables = CompositeDisposable()
    val pageId = MutableLiveData<String>()
    val markerLocation = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val imageList = MutableLiveData<List<String>>()
    val link = MutableLiveData<String>()
    val imageArrayList = ArrayList<String>()

    private var imagesTitles: String = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        pageId.observeForever {
            it?.let { pageId ->
                imageArrayList.clear()
                loadMarkerDescription(pageId)
            }
        }
    }

    private fun loadMarkerDescription(id: String) {
        disposables += markerDetailsUseCase
            .execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                for ((_, value) in it.query.pages) {
                    title.value = value.title
                    description.value = value.description
                    val url = BuildConfig.API_ARTICLE_URL + value.title.replace(" ", "_")
                    link.value = url
                    value.images.forEach { images ->
                        imagesTitles = imagesTitles.plus(images.title + "|")
                    }
                    imagesTitles = imagesTitles.substring(0, imagesTitles.length - 1)

                    loadImages(imagesTitles)
                    imagesTitles = ""
                }
                Log.d("test", it.query.toString())
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    private fun loadImages(id: String) {
        disposables += imageUseCase
            .execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribeBy(onSuccess = {
                for ((_, value) in it.query.pages) {
                    imageArrayList.add(value.imageInfo[0].thumburl)
                }
                imageList.value = imageArrayList
            }, onError = {
                Log.d("test", it.message ?: "")
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}